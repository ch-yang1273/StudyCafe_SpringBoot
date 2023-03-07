package asc.portfolio.ascSb.seatreservationinfo.service;

import asc.portfolio.ascSb.seatreservationinfo.domain.SeatReservationInfo;
import asc.portfolio.ascSb.user.domain.User;
import asc.portfolio.ascSb.seatreservationinfo.dto.SeatReservationInfoSelectResponseDto;

public interface SeatReservationInfoService {

    public SeatReservationInfoSelectResponseDto showUserSeatReservationInfo(String loginId, String cafeName);

    SeatReservationInfo validUserSeatReservationInfo(User user);

}
