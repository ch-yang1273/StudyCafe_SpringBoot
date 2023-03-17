package asc.portfolio.ascSb.seat.service;
import asc.portfolio.ascSb.user.domain.User;
import asc.portfolio.ascSb.seat.dto.SeatResponseDto;
import asc.portfolio.ascSb.seat.dto.SeatSelectResponseDto;

import java.io.IOException;
import java.util.List;

public interface SeatService {

    List<SeatSelectResponseDto> showCurrentAllSeatState(String cafeName);

    public SeatResponseDto showSeatStateOne(Long userId, Integer seatNumber);

    public Boolean exitSeat(Long userId);

    public Boolean reserveSeat(Long userId, Integer seatNumber, Long startTime);

    public void exitSeatBySeatNumber(Long adminId, int seatNumber);

    public int updateAllReservedSeatState();

    public void alertAlmostFinishedSeat() throws IOException;
}
