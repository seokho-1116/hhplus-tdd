package io.hhplus.tdd.point;

import io.hhplus.tdd.database.PointHistoryTable;
import io.hhplus.tdd.database.UserPointTable;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PointService {

  private final UserPointTable userPointTable;
  private final PointHistoryTable pointHistoryTable;
  private final ConcurrentHashMap<Long, ReentrantLock> lockMap = new ConcurrentHashMap<>();

  public UserPoint selectById(long userId) {
    return userPointTable.selectById(userId);
  }

  public List<PointHistory> selectAllByUserId(long userId) {
    return pointHistoryTable.selectAllByUserId(userId);
  }

  public UserPoint chargePoint(long userId, long amount) {
    ReentrantLock reentrantLock = lockMap.computeIfAbsent(userId, k -> new ReentrantLock());

    reentrantLock.lock();
    UserPoint chargedUserPoint;
    try {
      UserPoint userPoint = userPointTable.selectById(userId);

      chargedUserPoint = userPoint.chargePoint(amount);
      userPointTable.insertOrUpdate(userId, chargedUserPoint.point());
    } finally {
      reentrantLock.unlock();
    }

    pointHistoryTable.insert(userId, amount, TransactionType.CHARGE,
        chargedUserPoint.updateMillis());
    return chargedUserPoint;
  }

  public UserPoint usePoint(long userId, long amount) {
    ReentrantLock reentrantLock = lockMap.computeIfAbsent(userId, k -> new ReentrantLock());

    reentrantLock.lock();
    UserPoint usedUserPoint;
    try {
      UserPoint userPoint = userPointTable.selectById(userId);

      usedUserPoint = userPoint.usePoint(amount);
      userPointTable.insertOrUpdate(userId, usedUserPoint.point());
    } finally {
      reentrantLock.unlock();
    }

    pointHistoryTable.insert(userId, amount, TransactionType.USE, usedUserPoint.updateMillis());
    return usedUserPoint;
  }
}
