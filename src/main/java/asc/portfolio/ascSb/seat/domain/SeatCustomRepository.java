package asc.portfolio.ascSb.seat.domain;

import asc.portfolio.ascSb.cafe.domain.Cafe;
import asc.portfolio.ascSb.seat.dto.SeatSelectResponseDto;

import java.util.List;

public interface SeatCustomRepository {

    public int updateAllReservedSeatStateWithFixedTermTicket();

    public int updateAllReservedSeatStateWithPartTimeTicket();

    public int updateAllReservedSeatStateWithStartTime();

    public List<Seat> getAlmostFinishedSeatListWithFixedTermTicket(Long minute);

    public List<Seat> getAlmostFinishedSeatListWithStartTime(Long minute);

    List<SeatSelectResponseDto> findSeatNumberAndSeatStateList(String cafeName);

    Seat findByCafeAndSeatNumber(Cafe cafeObject, Integer seatNumber);

    Seat findByCafeNameAndSeatNumber(String cafeName, int seatNumber);
}
