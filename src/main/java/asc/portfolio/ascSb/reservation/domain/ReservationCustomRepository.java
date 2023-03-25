package asc.portfolio.ascSb.reservation.domain;

import asc.portfolio.ascSb.reservation.dto.ReservationResponse;

import java.util.List;

public interface ReservationCustomRepository {

  public List<Reservation> findValidReservationByLoginId(String loginId);

  ReservationResponse findReservationByUserIdAndCafeName(String loginId, String cafeName);

  public Reservation findValidReservationByCafeNameAndSeatNumber(String cafeName, Integer seatNumber);
}
