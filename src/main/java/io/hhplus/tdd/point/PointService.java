package io.hhplus.tdd.point;

import io.hhplus.tdd.database.UserPointTable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PointService {

  private final UserPointTable userPointTable;

  public UserPoint findPoint(long id) {
    return userPointTable.selectById(id);
  }
}
