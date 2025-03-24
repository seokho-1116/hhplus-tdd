package io.hhplus.tdd.point;

import io.hhplus.tdd.point.exception.ChargePointNegativeException;
import io.hhplus.tdd.point.exception.PointOverLimitException;
import io.hhplus.tdd.point.exception.PointUnderLimitException;
import io.hhplus.tdd.point.exception.UsePointNegativeException;

public record UserPoint(
        long id,
        long point,
        long updateMillis
) {

    private static final long MAX_POINT = 100_000;

    public static UserPoint empty(long id) {
        return new UserPoint(id, 0, System.currentTimeMillis());
    }

    public static long maxPoint() {
        return MAX_POINT;
    }

    public UserPoint charge(long chargeAmount) {
        if (chargeAmount < 0) {
            throw new ChargePointNegativeException("충전 포인트는 0보다 작을 수 없습니다.");
        }

        if (point + chargeAmount > MAX_POINT) {
            throw new PointOverLimitException("포인트가 한도를 초과했습니다.");
        }

        return new UserPoint(id, point + chargeAmount, updateMillis);
    }

    public UserPoint use(long useAmount) {
        if (useAmount < 0) {
            throw new UsePointNegativeException("사용 포인트는 0보다 작을 수 없습니다.");
        }

        if (point - useAmount < 0) {
            throw new PointUnderLimitException("포인트가 부족합니다.");
        }

        return new UserPoint(id, point - useAmount, updateMillis);
    }
}
