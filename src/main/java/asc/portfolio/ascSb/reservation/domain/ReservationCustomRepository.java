package asc.portfolio.ascSb.reservation.domain;

import java.util.Optional;

public interface ReservationCustomRepository {

    Optional<Reservation> findByUserIdAndInUseStatus(Long userId);

    Optional<Reservation> findBySeatIdAndInUseStatus(Long seatId);
}
