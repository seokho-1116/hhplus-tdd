package io.hhplus.tdd.point;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

import io.hhplus.tdd.database.UserPointTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserPointTest {

  @Mock
  private UserPointTable userPointTable;

  @InjectMocks
  private PointService pointService;

  // 테스트 작성 이유 - 성공 케이스 테스트를 위함
  @DisplayName("포인트가 있을 때 포인트를 조회하면 현재 포인트가 나와야 한다")
  @Test
  void findPointIfExistPointThenReturnCurrentPointTest() {
    //given
    long userId = 1;
    long point = 100;
    given(userPointTable.selectById(anyLong())).willReturn(new UserPoint(userId, point, 100));

    //when
    UserPoint userPoint = pointService.findPoint(userId);

    //then
    assertThat(userPoint.point()).isEqualTo(point);
  }

  // 테스트 작성 이유 - 성공 케이스 테스트를 위함
  @DisplayName("포인트가 있을 때 포인트를 조회하면 마지막으로 업데이트된 시간이 나와야 한다")
  @Test
  void findPointIfExistPointThenReturnUpdateMillsTest() {
    //given
    long userId = 1;
    long point = 100;
    long updateMillis = 100;
    given(userPointTable.selectById(anyLong())).willReturn(new UserPoint(userId, point, updateMillis));

    //when
    UserPoint userPoint = pointService.findPoint(userId);

    //then
    assertThat(userPoint.updateMillis()).isEqualTo(updateMillis);
  }

  // 테스트 작성 이유 - 성공 케이스 테스트를 위함과 포인트가 아예 존재하지 않더라도 0을 반환하는지 확인하기 위함
  @DisplayName("포인트가 없을 때 포인트를 조회하면 0이 나와야 한다")
  @Test
  void findPointIfNotExistPointThenReturnZeroTest() {
    //given
    long userId = 1;
    // 질문사항 - PointService만 테스트를 하려는 것인데 UserPointTable이 null을 반환하지 않는다는 것을 알아야만 하는지?
    // 더 좋은 방법이 있을까?
    given(userPointTable.selectById(anyLong())).willReturn(UserPoint.empty(userId));

    //when
    UserPoint userPoint = pointService.findPoint(userId);

    //then
    assertThat(userPoint.point()).isZero();
  }

  // 테스트 작성 이유 - 0이라는 특수한 케이스(경계값)를 테스트하기 위함
  @DisplayName("포인트가 0일 때 포인트를 조회하면 0이 나와야 한다")
  @Test
  void findPointIfZeroThenReturnZeroTest() {
    //given
    long userId = 1;
    given(userPointTable.selectById(anyLong())).willReturn(new UserPoint(userId, 0, 100));

    //when
    UserPoint userPoint = pointService.findPoint(userId);

    //then
    assertThat(userPoint.point()).isZero();
  }

  // 테스트 작성 이유 - 음수라는 특수한 케이스를 테스트하기 위함
  @DisplayName("포인트가 음수일 때 포인트를 조회하면 음수가 나와야 한다")
  @Test
  void findPointIfNegativeThenReturnZeroTest() {
    //given
    long userId = 1;
    given(userPointTable.selectById(anyLong())).willReturn(new UserPoint(userId, -1, 100));

    //when
    UserPoint userPoint = pointService.findPoint(userId);

    //then
    assertThat(userPoint.point()).isNegative();
  }

  // 테스트 작성 이유 - 최대치라는 특수한 케이스(경계값)를 테스트하기 위함
  @DisplayName("포인트가 최대치일 때 포인트를 조회하면 최대치가 나와야 한다")
  @Test
  void findPointIfMaxThenReturnMaxTest() {
    //given
    long maxPoint = UserPoint.maxPoint();
    long userId = 1;
    given(userPointTable.selectById(anyLong())).willReturn(new UserPoint(userId, maxPoint, 100));

    //when
    UserPoint userPoint = pointService.findPoint(userId);

    //then
    assertThat(userPoint.point()).isEqualTo(maxPoint);
  }

  // 테스트 작성 이유 - 최대치를 초과하는 값이라는 특수한 케이스를 테스트하기 위함
  @DisplayName("포인트가 최대치보다 클 때 포인트를 조회하면 현재 포인트가 나와야 한다")
  @Test
  void findPointIfOverMaxThenReturnMaxTest() {
    //given
    long point = UserPoint.maxPoint() + 1_000_000;
    long userId = 1;
    given(userPointTable.selectById(anyLong())).willReturn(new UserPoint(userId, point, 100));

    //when
    UserPoint userPoint = pointService.findPoint(userId);

    //then
    assertThat(userPoint.point()).isEqualTo(point);
  }
}
