package asc.portfolio.ascSb.reservation.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, Long>, ReservationCustomRepository {

    Optional<Reservation> findByUserIdAndStatus(Long userId, ReservationStatus isValid);
}