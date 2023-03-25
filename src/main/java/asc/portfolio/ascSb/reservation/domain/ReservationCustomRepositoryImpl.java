package asc.portfolio.ascSb.reservation.domain;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import asc.portfolio.ascSb.reservation.dto.ReservationResponse;
import com.querydsl.core.types.Projections;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Repository
public class ReservationCustomRepositoryImpl implements ReservationCustomRepository {

    private final JPAQueryFactory query;

    @Override
    public List<Reservation> findValidReservationByLoginId(String loginId) {

        List<Reservation> result = query
                .select(QReservation.reservation)
                .from(QReservation.reservation)
                .where(QReservation.reservation.userLoginId.eq(loginId),
                        QReservation.reservation.isValid.eq(ReservationStatus.VALID))
                .orderBy(QReservation.reservation.startTime.desc())
                .fetch();

        int listSize = result.size();
        if (listSize == 0) {
            // 정보 없음
            log.debug("No valid reservation. user = {}", loginId);
            return null;
        } else if (listSize > 1) {
            // Valid 상태의 reservation info 는 한개여야 한다. 런타임 예외 필요.
            log.error("The user[{}]'s valid state information exceed one.", loginId);
        }
        return result;
    }

    @Override
    public ReservationResponse findReservationByUserIdAndCafeName(String loginId, String cafeName) {
        return query
                .select(Projections.bean(ReservationResponse.class,
                        QReservation.reservation.seatNumber,
                        QReservation.reservation.startTime,
                        QReservation.reservation.timeInUse,
                        QReservation.reservation.createDate))
                .from(QReservation.reservation)
                .where(QReservation.reservation.userLoginId.eq(loginId),
                        QReservation.reservation.cafeName.eq(cafeName),
                        QReservation.reservation.isValid.eq(ReservationStatus.VALID))
                .fetchOne();

    }

    @Override
    public Reservation findValidReservationByCafeNameAndSeatNumber(String cafeName, Integer seatNumber) {
        return query
                .selectFrom(QReservation.reservation)
                .where(QReservation.reservation.isValid.eq(ReservationStatus.VALID),
                        QReservation.reservation.cafeName.eq(cafeName),
                        QReservation.reservation.seatNumber.eq(seatNumber))
                .fetchOne();
    }
}
