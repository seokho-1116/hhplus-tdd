package io.hhplus.tdd.point;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class PointServiceIntegrationTest {

  @Autowired
  private PointService pointService;

  @DisplayName("포인트를 충전하면 현재 포인트에서 충전한만큼 충전된다")
  @Test
  void chargePoint() {
    // given
    long userId = 1L;
    long amount = 100L;

    // when
    UserPoint userPoint = pointService.chargePoint(userId, amount);

    // then
    assertThat(userPoint.point()).isEqualTo(amount);
  }

  @DisplayName("포인트를 사용하면 현재 포인트에서 사용한만큼 차감된다")
  @Test
  void usePoint() {
    // given
    long userId = 2L;
    long amount = 1000L;
    pointService.chargePoint(userId, amount);
    long useAmount = 500L;

    // when
    UserPoint userPoint = pointService.usePoint(userId, useAmount);

    // then
    assertThat(userPoint.point()).isEqualTo(amount - useAmount);
  }
}