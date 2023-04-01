package asc.portfolio.ascSb.reservation.domain;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Repository;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Repository
public class ReservationCustomRepositoryImpl implements ReservationCustomRepository {

    private final JPAQueryFactory query;

    @Override
    public Optional<Reservation> findByUserIdAndInUseStatus(Long userId) {
        return Optional.ofNullable(query
                .selectFrom(QReservation.reservation)
                .where(QReservation.reservation.userId.eq(userId),
                        QReservation.reservation.status.eq(ReservationStatus.IN_USE))
                .fetchOne());
    }

    @Override
    public Optional<Reservation> findBySeatIdAndInUseStatus(Long seatId) {
        return Optional.ofNullable(query
                .selectFrom(QReservation.reservation)
                .where(QReservation.reservation.seatId.eq(seatId),
                        QReservation.reservation.status.eq(ReservationStatus.IN_USE))
                .fetchOne());
    }
}
