package asc.portfolio.ascSb.reservation.domain;

import org.springframework.data.jpa.repository.JpaRepository;


public interface ReservationRepository extends JpaRepository<Reservation, Long>, ReservationCustomRepository {

    Reservation findByUserIdAndStatusAndCafeId(Long userId, ReservationStatus isValid, Long cafeId); //todo 삭제 요망
}