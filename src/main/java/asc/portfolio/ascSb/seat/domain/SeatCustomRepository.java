package asc.portfolio.ascSb.seat.domain;

import java.time.LocalDateTime;
import java.util.List;

public interface SeatCustomRepository {

    List<Seat> findSeatsByStatusWithEndTimeAfter(SeatUsageStatus usageStatus, LocalDateTime time);
}
