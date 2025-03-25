package io.hhplus.tdd.point;

public record UserPoint(
        long id,
        long point,
        long updateMillis
) {

    private static final long MAX_POINT = 1_000_000;

    public static UserPoint empty(long id) {
        return new UserPoint(id, 0, System.currentTimeMillis());
    }

    public static long maxPoint() {
        return MAX_POINT;
    }

    public UserPoint chargePoint(long amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("음수 포인트는 충전할 수 없습니다.");
        }

        if (point + amount > MAX_POINT) {
            throw new IllegalArgumentException("포인트 최대치를 초과했습니다.");
        }

        return new UserPoint(id, point + amount, System.currentTimeMillis());
    }

    public UserPoint usePoint(long amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("음수 포인트는 사용할 수 없습니다.");
        }

        if (point - amount < 0) {
            throw new IllegalArgumentException("포인트가 부족합니다.");
        }

        return new UserPoint(id, point - amount, System.currentTimeMillis());
    }
}
