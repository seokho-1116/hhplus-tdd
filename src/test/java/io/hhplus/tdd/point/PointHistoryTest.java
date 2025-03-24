package io.hhplus.tdd.point;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

import io.hhplus.tdd.database.PointHistoryTable;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PointHistoryTest {

  @Mock
  private PointHistoryTable pointHistoryTable;

  @InjectMocks
  private PointService pointService;

  @DisplayName("포인트 충전 후 포인트 충전 내역을 조회하면 포인트 충전 내역의 개수가 1개여야 한다")
  @Test
  void findPointHistoryAfterChargeThenReturnChargeHistorySizeTest() {
    //given
    long userId = 1;
    given(pointHistoryTable.selectAllByUserId(anyLong())).willReturn(
        List.of(new PointHistory(1, userId, 100, TransactionType.CHARGE, 100)));

    //when
    List<PointHistory> pointHistories = pointService.findPointHistories(userId);

    //then
    assertThat(pointHistories).hasSize(1);
  }

  @DisplayName("포인트를 n회 충전 후 포인트 충전 내역을 조회하면 포인트 충전 내역의 개수가 n개여야 한다")
  @Test
  void findPointHistoryAfterChargeMultipleTimesThenReturnChargeHistorySizeTest() {
    //given
    long userId = 1;
    given(pointHistoryTable.selectAllByUserId(anyLong())).willReturn(
        List.of(new PointHistory(1, userId, 100, TransactionType.CHARGE, 100),
            new PointHistory(2, userId, 200, TransactionType.CHARGE, 200),
            new PointHistory(3, userId, 300, TransactionType.CHARGE, 300)));

    //when
    List<PointHistory> pointHistories = pointService.findPointHistories(userId);

    //then
    assertThat(pointHistories).hasSize(3);
  }

  @DisplayName("포인트 충전/사용 내역이 없을 때 포인트 충전/사용 내역을 조회하면 빈 목록이 나와야 한다")
  @Test
  void findPointHistoryIfNotExistThenReturnEmptyListTest() {
    //given
    long userId = 1;
    given(pointHistoryTable.selectAllByUserId(anyLong())).willReturn(List.of());

    //when
    List<PointHistory> pointHistories = pointService.findPointHistories(userId);

    //then
    assertThat(pointHistories).isEmpty();
  }

  @DisplayName("포인트 사용 후 포인트 사용 내역을 조회하면 포인트 사용 내역의 개수가 1개여야 한다")
  @Test
  void findPointHistoryAfterChargeThenReturnUseHistorySizeTest() {
    //given
    long userId = 1;
    given(pointHistoryTable.selectAllByUserId(anyLong())).willReturn(
        List.of(new PointHistory(1, userId, 100, TransactionType.USE, 100)));

    //when
    List<PointHistory> pointHistories = pointService.findPointHistories(userId);

    //then
    assertThat(pointHistories).hasSize(1);
  }

  @DisplayName("포인트를 n회 사용 후 포인트 사용 내역을 조회하면 포인트 사용 내역의 개수가 n개여야 한다")
  @Test
  void findPointHistoryAfterChargeMultipleTimesThenReturnUseHistorySizeTest() {
    //given
    long userId = 1;
    given(pointHistoryTable.selectAllByUserId(anyLong())).willReturn(
        List.of(new PointHistory(1, userId, 100, TransactionType.USE, 100),
            new PointHistory(2, userId, 200, TransactionType.USE, 200),
            new PointHistory(3, userId, 300, TransactionType.USE, 300)));

    //when
    List<PointHistory> pointHistories = pointService.findPointHistories(userId);

    //then
    assertThat(pointHistories).hasSize(3);
  }

  @DisplayName("포인트를 n회 충전/사용 후 포인트 충전/사용 내역을 조회하면 포인트 충전/사용 내역의 개수가 n개여야 한다")
  @Test
  void findPointHistoryAfterChargeAndUseMultipleTimesThenReturnChargeAndUseHistorySizeTest() {
    //given
    long userId = 1;
    given(pointHistoryTable.selectAllByUserId(anyLong())).willReturn(
        List.of(new PointHistory(1, userId, 100, TransactionType.CHARGE, 100),
            new PointHistory(2, userId, 200, TransactionType.USE, 200),
            new PointHistory(3, userId, 300, TransactionType.CHARGE, 300),
            new PointHistory(4, userId, 400, TransactionType.USE, 400)));

    //when
    List<PointHistory> pointHistories = pointService.findPointHistories(userId);

    //then
    assertThat(pointHistories).hasSize(4);
  }
}
