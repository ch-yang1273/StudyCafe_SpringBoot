package asc.portfolio.ascSb.seat.domain;

import java.util.List;
import java.util.Optional;

public interface SeatCustomRepository {
    Optional<Seat> findOptionalByUserId(Long userId);

    int updateAllReservedSeatStateWithFixedTermTicket();

    int updateAllReservedSeatStateWithPartTimeTicket();

    int updateAllReservedStatusWithStartTime();

    List<Seat> getAlmostFinishedSeatListWithFixedTermTicket(Long minute);

    List<Seat> getAlmostFinishedSeatListWithStartTime(Long minute);
}
