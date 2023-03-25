package asc.portfolio.ascSb.reservation.service;

import asc.portfolio.ascSb.reservation.domain.Reservation;
import asc.portfolio.ascSb.reservation.dto.ReservationResponse;

public interface ReservationService {

    ReservationResponse getReservation(Long userId);

    Reservation getValidReservation(Long userId);

}
