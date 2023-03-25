package asc.portfolio.ascSb.reservation.domain;

import asc.portfolio.ascSb.reservation.dto.ReservationResponse;

import java.util.List;

public interface ReservationCustomRepository {

    public List<Reservation> findValidReservationByLoginId(Long userId);

    ReservationResponse findReservationByUserIdAndCafeName(Long userId, Long cafeId);

    public Reservation findValidReservationByCafeNameAndSeatNumber(Long cafeId, Long seatId);
}
