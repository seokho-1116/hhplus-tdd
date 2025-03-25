package io.hhplus.tdd.point;

import io.hhplus.tdd.database.PointHistoryTable;
import io.hhplus.tdd.database.UserPointTable;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PointService {

  private final UserPointTable userPointTable;
  private final PointHistoryTable pointHistoryTable;

  public UserPoint selectById(long userId) {
    return userPointTable.selectById(userId);
  }

  public List<PointHistory> selectAllByUserId(long userId) {
    return pointHistoryTable.selectAllByUserId(userId);
  }

  public UserPoint chargePoint(long userId, long amount) {
    UserPoint userPoint = userPointTable.selectById(userId);

    UserPoint chargedUserPoint = userPoint.chargePoint(amount);
    userPointTable.insertOrUpdate(userId, chargedUserPoint.point());

    pointHistoryTable.insert(userId, amount, TransactionType.CHARGE, chargedUserPoint.updateMillis());
    return chargedUserPoint;
  }

  public UserPoint usePoint(long userId, long amount) {
    UserPoint userPoint = userPointTable.selectById(userId);

    UserPoint usedUserPoint = userPoint.usePoint(amount);
    userPointTable.insertOrUpdate(userId, usedUserPoint.point());

    pointHistoryTable.insert(userId, amount, TransactionType.USE, usedUserPoint.updateMillis());
    return usedUserPoint;
  }
}
