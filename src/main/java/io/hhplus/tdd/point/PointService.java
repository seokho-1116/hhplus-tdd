package io.hhplus.tdd.point;

import io.hhplus.tdd.database.PointHistoryTable;
import io.hhplus.tdd.database.UserPointTable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PointService {

  private final UserPointTable userPointTable;
  private final PointHistoryTable pointHistoryTable;

  public UserPoint findPoint(long userId) {
    return userPointTable.selectById(userId);
  }

  public UserPoint chargePoint(long userId, long chargeAmount) {
    UserPoint currentUserPoint = userPointTable.selectById(userId);

    UserPoint afterCharge = currentUserPoint.charge(chargeAmount);
    UserPoint chargedPoint = userPointTable.insertOrUpdate(userId, afterCharge.point());

    pointHistoryTable.insert(userId, chargeAmount, TransactionType.CHARGE, System.currentTimeMillis());
    return chargedPoint;
  }

  public UserPoint usePoint(long userId, long useAmount) {
    UserPoint currentUserPoint = userPointTable.selectById(userId);

    UserPoint afterUse = currentUserPoint.use(useAmount);
    UserPoint usedPoint = userPointTable.insertOrUpdate(userId, afterUse.point());

    pointHistoryTable.insert(userId, useAmount, TransactionType.USE, System.currentTimeMillis());
    return usedPoint;
  }
}
