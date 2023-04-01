package asc.portfolio.ascSb.seat.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface SeatCustomRepository {

    Optional<Seat> findByUserId(Long userId);

    List<Seat> findSeatsByStatusWithEndTimeAfter(SeatUsageStatus usageStatus, LocalDateTime time);
}
