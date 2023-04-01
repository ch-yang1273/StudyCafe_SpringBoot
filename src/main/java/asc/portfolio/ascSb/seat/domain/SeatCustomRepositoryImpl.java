package asc.portfolio.ascSb.seat.domain;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Repository
public class SeatCustomRepositoryImpl implements SeatCustomRepository {

    private final JPAQueryFactory query;

    @Override
    public List<Seat> findSeatsByStatusWithEndTimeAfter(SeatUsageStatus usageStatus, LocalDateTime time) {
        return query.selectFrom(QSeat.seat)
                .where(QSeat.seat.usageStatus.eq(usageStatus),
                        QSeat.seat.usageData.endTime.after(time))
                .fetch();
    }
}
