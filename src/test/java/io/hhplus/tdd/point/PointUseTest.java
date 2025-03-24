package io.hhplus.tdd.point;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

import io.hhplus.tdd.database.PointHistoryTable;
import io.hhplus.tdd.database.UserPointTable;
import io.hhplus.tdd.point.exception.PointUnderLimitException;
import io.hhplus.tdd.point.exception.UsePointNegativeException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PointUseTest {

  @Mock
  private PointHistoryTable pointHistoryTable;

  @Mock
  private UserPointTable userPointTable;

  @InjectMocks
  private PointService pointService;

  @DisplayName("포인트 사용 시 기존 포인트가 있으면 기존 포인트에서 사용 포인트를 뺀 값을 반환한다.")
  @Test
  void usePointIfExistPointThenReturnSubtractPointTest() {
    //given
    long userId = 1;
    long point = 1000;
    long useAmount = 100;
    given(userPointTable.selectById(anyLong())).willReturn(new UserPoint(userId, point, 1000));
    given(userPointTable.insertOrUpdate(anyLong(), anyLong())).willReturn(
        new UserPoint(userId, point - useAmount, 1000));

    //when
    UserPoint userPoint = pointService.usePoint(userId, useAmount);

    //then
    assertThat(userPoint.point()).isEqualTo(point - useAmount);
  }

  @DisplayName("포인트 사용 시 기존 포인트가 0이면 예외를 반환한다.")
  @Test
  void usePointIfNotExistPointThenThrowExceptionTest() {
    //given
    long userId = 1;
    long point = 0;
    long useAmount = 100;
    given(userPointTable.selectById(anyLong())).willReturn(new UserPoint(userId, point, 1000));

    //when
    //then
    assertThatThrownBy(() -> pointService.usePoint(userId, useAmount))
        .isInstanceOf(PointUnderLimitException.class);
  }

  @DisplayName("포인트 사용 시 사용 후 포인트가 0이면 0을 반환한다.")
  @Test
  void usePointIfZeroThenReturnZeroTest() {
    //given
    long userId = 1;
    long point = 100;
    long useAmount = 100;
    given(userPointTable.selectById(anyLong())).willReturn(new UserPoint(userId, point, 1000));
    given(userPointTable.insertOrUpdate(anyLong(), anyLong())).willReturn(
        new UserPoint(userId, 0, 1000));

    //when
    UserPoint userPoint = pointService.usePoint(userId, useAmount);

    //then
    assertThat(userPoint.point()).isZero();
  }

  @DisplayName("포인트 사용 시 최대 포인트인 경우 최대 포인트에서 사용 포인트를 뺀 값을 반환한다.")
  @Test
  void usePointIfMaxThenReturnSubtractPointTest() {
    //given
    long userId = 1;
    long point = UserPoint.maxPoint();
    long useAmount = 100;
    given(userPointTable.selectById(anyLong())).willReturn(new UserPoint(userId, point, 1000));
    given(userPointTable.insertOrUpdate(anyLong(), anyLong())).willReturn(
        new UserPoint(userId, point - useAmount, 1000));

    //when
    UserPoint userPoint = pointService.usePoint(userId, useAmount);

    //then
    assertThat(userPoint.point()).isEqualTo(point - useAmount);
  }

  @DisplayName("포인트 사용 시 포인트가 0보다 작아진다면 예외를 반환한다.")
  @Test
  void usePointIfNegativeThenThrowExceptionTest() {
    //given
    long userId = 1;
    long point = 100;
    long useAmount = 200;
    given(userPointTable.selectById(anyLong())).willReturn(new UserPoint(userId, point, 1000));

    //when
    //then
    assertThatThrownBy(() -> pointService.usePoint(userId, useAmount))
        .isInstanceOf(PointUnderLimitException.class);
  }

  @DisplayName("포인트 사용 시 포인트 사용량이 음수면 예외를 반환한다.")
  @Test
  void usePointIfNegativeAmountThenThrowExceptionTest() {
    //given
    long userId = 1;
    long point = 100;
    long useAmount = -100;
    given(userPointTable.selectById(anyLong())).willReturn(new UserPoint(userId, point, 1000));

    //when
    //then
    assertThatThrownBy(() -> pointService.usePoint(userId, useAmount))
        .isInstanceOf(UsePointNegativeException.class);
  }
}
