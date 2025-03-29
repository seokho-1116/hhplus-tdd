package io.hhplus.tdd.point;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.CountDownLatch;
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

  @DisplayName("동시에 포인트를 충전하면 충전된 포인트만큼만 증가한다")
  @Test
  void chargePointConcurrently() throws InterruptedException {
    // given
    long userId = 3L;
    long amount = 100L;

    int concurrentCount = 5;
    CountDownLatch latch = new CountDownLatch(concurrentCount);

    // when
    for (int userRequest = 0; userRequest < concurrentCount; userRequest++) {
      new Thread(() -> {
        pointService.chargePoint(userId, amount);
        latch.countDown();
      }).start();
    }

    latch.await();

    // then
    UserPoint userPoint = pointService.selectById(userId);
    assertThat(userPoint.point()).isEqualTo(amount * concurrentCount);
  }

  @DisplayName("동시에 포인트를 사용하면 사용된 포인트만큼만 차감된다")
  @Test
  void usePointConcurrently() throws InterruptedException {
    // given
    long userId = 4L;
    long amount = 100000L;
    long useAmount = 500L;
    pointService.chargePoint(userId, amount);

    int concurrentCount = 5;
    CountDownLatch latch = new CountDownLatch(concurrentCount);

    // when
    for (int userRequest = 0; userRequest < concurrentCount; userRequest++) {
      new Thread(() -> {
        pointService.usePoint(userId, useAmount);
        latch.countDown();
      }).start();
    }

    latch.await();

    // then
    UserPoint userPoint = pointService.selectById(userId);
    assertThat(userPoint.point()).isEqualTo(amount - (useAmount * concurrentCount));
  }
}