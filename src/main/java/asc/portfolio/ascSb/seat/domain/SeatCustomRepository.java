package asc.portfolio.ascSb.seat.domain;

import asc.portfolio.ascSb.cafe.domain.Cafe;

import java.util.List;

public interface SeatCustomRepository {

    int updateAllReservedSeatStateWithFixedTermTicket();

    int updateAllReservedSeatStateWithPartTimeTicket();

    int updateAllReservedSeatStateWithStartTime();

    List<Seat> getAlmostFinishedSeatListWithFixedTermTicket(Long minute);

    List<Seat> getAlmostFinishedSeatListWithStartTime(Long minute);

    Seat findByCafeAndSeatNumber(Cafe cafeObject, Integer seatNumber);

    Seat findByCafeNameAndSeatNumber(String cafeName, int seatNumber);
}
