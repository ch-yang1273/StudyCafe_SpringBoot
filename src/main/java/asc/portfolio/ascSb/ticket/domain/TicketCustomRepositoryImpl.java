package asc.portfolio.ascSb.ticket.domain;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Slf4j
public class TicketCustomRepositoryImpl implements TicketCustomRepository {

    private final JPAQueryFactory query;

    @Override
    public Optional<Ticket> findTicketByUserIdAndCafeIdAndTicketStatus(Long userId, Long cafeId, TicketStatus status) {
        return Optional.ofNullable(query
                .selectFrom(QTicket.ticket)
                .where(QTicket.ticket.userId.eq(userId),
                        QTicket.ticket.cafeId.eq(cafeId),
                        QTicket.ticket.status.eq(status))
                .fetchOne());
    }
}


