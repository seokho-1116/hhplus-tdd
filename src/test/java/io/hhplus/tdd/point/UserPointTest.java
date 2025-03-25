package io.hhplus.tdd.point;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UserPointTest {

  @DisplayName("포인트 충전 시 기존 포인트가 있다면 기존 포인트에 충전 포인트를 더한 값을 반환한다.")
  @Test
  void chargePointIfExistPointThenReturnSumPointTest() {
    //given
    UserPoint userPoint = new UserPoint(1, 100, System.currentTimeMillis());

    //when
    UserPoint chargedPoint = userPoint.chargePoint(100);

    //then
    assertThat(chargedPoint.point()).isEqualTo(200);
  }

  @DisplayName("포인트 충전 시 기존 포인트가 0이라면 충전 포인트를 반환한다.")
  @Test
  void chargePointIfNotExistPointThenReturnChargePointTest() {
    //given
    UserPoint userPoint = new UserPoint(1, 0, System.currentTimeMillis());

    //when
    UserPoint chargedPoint = userPoint.chargePoint(100);

    //then
    assertThat(chargedPoint.point()).isEqualTo(100);
  }

  @DisplayName("음수 포인트를 충전 시 예외를 반환한다.")
  @Test
  void chargePointIfNegativeThenThrowExceptionTest() {
    //given
    UserPoint userPoint = new UserPoint(1, 100, System.currentTimeMillis());

    //when
    //then
    assertThatThrownBy(() -> userPoint.chargePoint(-100))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @DisplayName("포인트 충전 시 최대 포인트를 초과했을 때 예외를 반환한다.")
  @Test
  void chargePointIfOverMaxThenThrowExceptionTest() {
    //given
    UserPoint userPoint = new UserPoint(1, UserPoint.maxPoint(), System.currentTimeMillis());

    //when
    //then
    assertThatThrownBy(() -> userPoint.chargePoint(1))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @DisplayName("포인트 충전 시 최대 포인트에 도달하면 최대 포인트를 반환한다.")
  @Test
  void chargePointIfReachMaxThenReturnMaxTest() {
    //given
    UserPoint userPoint = new UserPoint(1, UserPoint.maxPoint() - 1, System.currentTimeMillis());

    //when
    UserPoint chargedPoint = userPoint.chargePoint(1);

    //then
    assertThat(chargedPoint.point()).isEqualTo(UserPoint.maxPoint());
  }

  @DisplayName("포인트 사용 시 기존 포인트가 있으면 기존 포인트에서 사용 포인트를 뺀 값을 반환한다.")
  @Test
  void usePointIfExistPointThenReturnSubtractPointTest() {
    //given
    UserPoint userPoint = new UserPoint(1, 100, System.currentTimeMillis());

    //when
    UserPoint usedPoint = userPoint.usePoint(50);

    //then
    assertThat(usedPoint.point()).isEqualTo(50);
  }

  @DisplayName("포인트 사용 시 기존 포인트가 0이면 예외를 반환한다.")
  @Test
  void usePointIfNotExistPointThenThrowExceptionTest() {
    //given
    UserPoint userPoint = new UserPoint(1, 0, System.currentTimeMillis());

    //when
    //then
    assertThatThrownBy(() -> userPoint.usePoint(100))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @DisplayName("포인트 사용 시 최대 포인트인 경우 최대 포인트에서 사용 포인트를 뺀 값을 반환한다.")
  @Test
  void usePointIfMaxThenReturnSubtractPointTest() {
    //given
    UserPoint userPoint = new UserPoint(1, UserPoint.maxPoint(), System.currentTimeMillis());

    //when
    UserPoint usedPoint = userPoint.usePoint(100);

    //then
    assertThat(usedPoint.point()).isEqualTo(UserPoint.maxPoint() - 100);
  }

  @DisplayName("포인트 사용 시 포인트가 0보다 작아진다면 예외를 반환한다.")
  @Test
  void usePointIfNegativeThenThrowExceptionTest() {
    //given
    UserPoint userPoint = new UserPoint(1, 100, System.currentTimeMillis());

    //when
    //then
    assertThatThrownBy(() -> userPoint.usePoint(200))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @DisplayName("포인트 사용 시 포인트 사용량이 음수면 예외를 반환한다.")
  @Test
  void usePointIfNegativeAmountThenThrowExceptionTest() {
    //given
    UserPoint userPoint = new UserPoint(1, 100, System.currentTimeMillis());

    //when
    //then
    assertThatThrownBy(() -> userPoint.usePoint(-100))
        .isInstanceOf(IllegalArgumentException.class);
  }
}