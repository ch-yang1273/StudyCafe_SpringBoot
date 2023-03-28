package asc.portfolio.ascSb.seat.service;
import asc.portfolio.ascSb.seat.dto.SeatStatusResponse;

import java.io.IOException;
import java.util.List;

public interface SeatService {

    SeatStatusResponse getSeatStatus(Long userId);

    List<SeatStatusResponse> getAllSeatsByCafeId(Long cafeId);

    int updateAllReservedSeatState();

    void alertAlmostFinishedSeat() throws IOException;
}
