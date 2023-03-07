package asc.portfolio.ascSb.seat.service;
import asc.portfolio.ascSb.cafe.domain.Cafe;
import asc.portfolio.ascSb.user.domain.User;
import asc.portfolio.ascSb.seat.dto.SeatResponseDto;
import asc.portfolio.ascSb.seat.dto.SeatSelectResponseDto;

import java.io.IOException;
import java.util.List;

public interface SeatService {

    List<SeatSelectResponseDto> showCurrentAllSeatState(String cafeName);

    public SeatResponseDto showSeatStateOne(String cafeName, Integer seatNumber);

    public Boolean exitSeat(User user);

    public Boolean reserveSeat(User user, Integer seatNumber, Long startTime);

    public void exitSeatBySeatNumber(Cafe cafe, int seatNumber);

    public int updateAllReservedSeatState();

    public void alertAlmostFinishedSeat() throws IOException;
}
