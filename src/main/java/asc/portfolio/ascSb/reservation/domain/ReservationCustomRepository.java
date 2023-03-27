package asc.portfolio.ascSb.reservation.domain;

import java.util.List;

public interface ReservationCustomRepository {

    List<Reservation> findListByUserIdAndStatus(Long userId, ReservationStatus status);
    
    List<Reservation> findListBySeatIdAndStatus(Long seatId, ReservationStatus status);
}
