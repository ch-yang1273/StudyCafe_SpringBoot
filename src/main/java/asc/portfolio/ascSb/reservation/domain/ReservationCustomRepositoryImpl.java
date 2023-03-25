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
    public List<Reservation> findValidReservationByLoginId(Long userId) {

        List<Reservation> result = query
                .select(QReservation.reservation)
                .from(QReservation.reservation)
                .where(QReservation.reservation.userId.eq(userId),
                        QReservation.reservation.status.eq(ReservationStatus.IN_USE))
                .orderBy(QReservation.reservation.startTime.desc())
                .fetch();

        int listSize = result.size();
        if (listSize == 0) {
            // 정보 없음
            log.debug("No valid reservation. user = {}", userId);
            return null;
        } else if (listSize > 1) {
            // Valid 상태의 reservation info 는 한개여야 한다. 런타임 예외 필요.
            log.error("The user[{}]'s valid state information exceed one.", userId);
        }
        return result;
    }

    @Override
    public ReservationResponse findReservationByUserIdAndCafeName(Long userId, Long cafeId) {
        return query
                .select(Projections.bean(ReservationResponse.class,
                        QReservation.reservation.seatId,
                        QReservation.reservation.startTime,
                        QReservation.reservation.status,
                        QReservation.reservation.createDate))
                .from(QReservation.reservation)
                .where(QReservation.reservation.userId.eq(userId),
                        QReservation.reservation.cafeId.eq(cafeId),
                        QReservation.reservation.status.eq(ReservationStatus.IN_USE))
                .fetchOne();

    }

    @Override
    public Reservation findValidReservationByCafeNameAndSeatNumber(Long cafeId, Long seatId) {
        return query
                .selectFrom(QReservation.reservation)
                .where(QReservation.reservation.status.eq(ReservationStatus.IN_USE),
                        QReservation.reservation.cafeId.eq(cafeId),
                        QReservation.reservation.seatId.eq(seatId))
                .fetchOne();
    }
}
