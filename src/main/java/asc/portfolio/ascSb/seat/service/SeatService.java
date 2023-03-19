package asc.portfolio.ascSb.seat.service;
import asc.portfolio.ascSb.seat.dto.SeatResponseDto;

import java.io.IOException;

public interface SeatService {

    SeatResponseDto showSeatStateOne(Long userId, Integer seatNumber);

    Boolean exitSeat(Long userId);

    Boolean reserveSeat(Long userId, Integer seatNumber, Long startTime);

    void exitSeatBySeatNumber(Long adminId, int seatNumber);

    int updateAllReservedSeatState();

    void alertAlmostFinishedSeat() throws IOException;
}
