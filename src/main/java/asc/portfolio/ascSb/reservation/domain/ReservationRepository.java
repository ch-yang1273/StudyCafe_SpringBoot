package asc.portfolio.ascSb.reservation.domain;

import org.springframework.data.jpa.repository.JpaRepository;


public interface ReservationRepository extends JpaRepository<Reservation, Long>, ReservationCustomRepository {

    Reservation findByUserLoginIdAndIsValidAndCafeName(String userLoginId, ReservationStatus isValid, String cafeName);

}