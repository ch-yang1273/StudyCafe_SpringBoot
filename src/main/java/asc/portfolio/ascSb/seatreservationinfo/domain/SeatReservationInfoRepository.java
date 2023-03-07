package asc.portfolio.ascSb.seatreservationinfo.domain;

import org.springframework.data.jpa.repository.JpaRepository;


public interface SeatReservationInfoRepository extends JpaRepository<SeatReservationInfo, Long>, SeatReservationInfoCustomRepository {

    SeatReservationInfo findByUserLoginIdAndIsValidAndCafeName(String userLoginId, SeatReservationInfoStateType isValid, String cafeName);

}