package asc.portfolio.ascSb.seat.service;
import asc.portfolio.ascSb.seat.dto.SeatStatusResponse;

import java.io.IOException;

public interface SeatService {

    SeatStatusResponse getSeatStatus(Long userId);

    int updateAllReservedSeatState();

    void alertAlmostFinishedSeat() throws IOException;
}
