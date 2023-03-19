package asc.portfolio.ascSb.seatreservationinfo.service;

import asc.portfolio.ascSb.seatreservationinfo.domain.SeatReservationInfo;
import asc.portfolio.ascSb.seatreservationinfo.dto.SeatReservationInfoSelectResponseDto;

public interface SeatReservationInfoService {

    SeatReservationInfoSelectResponseDto showUserSeatReservationInfo(Long userId);

    SeatReservationInfo validUserSeatReservationInfo(Long userId);

}
