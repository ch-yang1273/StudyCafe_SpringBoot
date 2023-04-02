package asc.portfolio.ascSb.seat.domain;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Repository
public class SeatCustomRepositoryImpl implements SeatCustomRepository {

    private final JPAQueryFactory query;

    @Override
    public Optional<Seat> findByUserId(Long userId) {
        return Optional.ofNullable(query.selectFrom(QSeat.seat)
                .where(QSeat.seat.usageData.userId.eq(userId))
                .fetchOne());
    }

    @Override
    public List<Seat> findSeatsByStatusWithEndTimeBefore(SeatUsageStatus usageStatus, LocalDateTime time) {
        return query.selectFrom(QSeat.seat)
                .where(QSeat.seat.usageStatus.eq(usageStatus),
                        QSeat.seat.usageData.endTime.before(time))
                .fetch();
    }
}
