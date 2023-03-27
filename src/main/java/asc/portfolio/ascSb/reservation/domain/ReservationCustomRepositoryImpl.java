package asc.portfolio.ascSb.reservation.domain;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Repository
public class ReservationCustomRepositoryImpl implements ReservationCustomRepository {

    private final JPAQueryFactory query;

    @Override
    public List<Reservation> findListByUserIdAndStatus(Long userId, ReservationStatus status) {
        return query
                .selectFrom(QReservation.reservation)
                .where(QReservation.reservation.userId.eq(userId),
                        QReservation.reservation.status.eq(status))
                .fetch();
    }

    @Override
    public List<Reservation> findListBySeatIdAndStatus(Long seatId, ReservationStatus status) {
        return query
                .selectFrom(QReservation.reservation)
                .where(QReservation.reservation.seatId.eq(seatId),
                QReservation.reservation.status.eq(status))
                .fetch();
    }
}
