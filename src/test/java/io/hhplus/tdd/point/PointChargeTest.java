package io.hhplus.tdd.point;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

import io.hhplus.tdd.database.PointHistoryTable;
import io.hhplus.tdd.database.UserPointTable;
import io.hhplus.tdd.point.exception.ChargePointNegativeException;
import io.hhplus.tdd.point.exception.PointOverLimitException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PointChargeTest {

  @Mock
  private PointHistoryTable pointHistoryTable;

  @Mock
  private UserPointTable userPointTable;

  @InjectMocks
  private PointService pointService;

  @DisplayName("포인트 충전 시 기존 포인트가 있다면 기존 포인트에 충전 포인트를 더한 값을 반환한다.")
  @Test
  void chargePointIfExistPointThenReturnSumPointTest() {
    //given
    long userId = 1;
    long point = 100;
    long chargeAmount = 100;
    given(userPointTable.selectById(anyLong())).willReturn(new UserPoint(userId, point, 100));
    given(userPointTable.insertOrUpdate(anyLong(), anyLong())).willReturn(new UserPoint(userId, point + chargeAmount, 100));

    //when
    UserPoint userPoint = pointService.chargePoint(userId, chargeAmount);

    //then
    assertThat(userPoint.point()).isEqualTo(point + chargeAmount);
  }

  @DisplayName("포인트 충전 시 첫 충전인 경우 충전 포인트를 반환한다.")
  @Test
  void chargePointIfFirstChargeThenReturnChargePointTest() {
    //given
    long userId = 1;
    long chargeAmount = 100;
    given(userPointTable.selectById(anyLong())).willReturn(UserPoint.empty(userId));
    given(userPointTable.insertOrUpdate(anyLong(), anyLong())).willReturn(new UserPoint(userId, chargeAmount, 100));

    //when
    UserPoint userPoint = pointService.chargePoint(userId, chargeAmount);

    //then
    assertThat(userPoint.point()).isEqualTo(chargeAmount);
  }

  @DisplayName("포인트 충전 시 기존 포인트가 0이라면 충전 포인트를 반환한다.")
  @Test
  void chargePointIfNotExistPointThenReturnChargePointTest() {
    //given
    long userId = 1;
    long point = 0;
    long chargeAmount = 100;
    given(userPointTable.selectById(anyLong())).willReturn(new UserPoint(userId, point, 100));
    given(userPointTable.insertOrUpdate(anyLong(), anyLong())).willReturn(new UserPoint(userId, point + chargeAmount, 100));

    //when
    UserPoint userPoint = pointService.chargePoint(userId, chargeAmount);

    //then
    assertThat(userPoint.point()).isEqualTo(chargeAmount);
  }

  @DisplayName("음수 포인트를 충전 시 예외를 반환한다.")
  @Test
  void chargePointIfNegativeThenThrowExceptionTest() {
    //given
    long userId = 1;
    long chargeAmount = -100;
    given(userPointTable.selectById(anyLong())).willReturn(new UserPoint(userId, 100, 100));

    //when
    //then
    assertThatThrownBy(() -> pointService.chargePoint(userId, chargeAmount))
            .isInstanceOf(ChargePointNegativeException.class);
  }

  @DisplayName("포인트 충전 시 최대 포인트를 초과했을 때 예외를 반환한다.")
  @Test
  void chargePointIfOverMaxThenThrowExceptionTest() {
    //given
    long userId = 1;
    long point = UserPoint.maxPoint();
    long chargeAmount = 100;
    given(userPointTable.selectById(anyLong())).willReturn(new UserPoint(userId, point, 100));

    //when
    //then
    assertThatThrownBy(() -> pointService.chargePoint(userId, chargeAmount))
            .isInstanceOf(PointOverLimitException.class);
  }

  @DisplayName("포인트 충전 시 최대 포인트에 도달하면 최대 포인트를 반환한다.")
  @Test
  void chargePointIfReachMaxThenReturnMaxTest() {
    //given
    long userId = 1;
    long chargeAmount = 100;
    long point = UserPoint.maxPoint() - chargeAmount;
    given(userPointTable.selectById(anyLong())).willReturn(new UserPoint(userId, point, 100));
    given(userPointTable.insertOrUpdate(anyLong(), anyLong())).willReturn(new UserPoint(userId, UserPoint.maxPoint(), 100));

    //when
    UserPoint userPoint = pointService.chargePoint(userId, chargeAmount);

    //then
    assertThat(userPoint.point()).isEqualTo(UserPoint.maxPoint());
  }
}
