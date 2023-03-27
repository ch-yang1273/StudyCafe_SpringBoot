package asc.portfolio.ascSb.reservation.service;

import asc.portfolio.ascSb.reservation.dto.CreateReservationRequest;
import asc.portfolio.ascSb.reservation.dto.ReservationResponse;

public interface ReservationService {

    ReservationResponse getReservation(Long userId);

    void createReservation(Long userId, CreateReservationRequest dto);

    void releaseReservation(Long userId);

    void releaseReservationByAdmin(Long adminId, Long cafeId, Long seatId);
}
