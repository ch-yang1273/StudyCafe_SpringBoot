package asc.portfolio.ascSb.ticket.domain;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Slf4j
public class TicketCustomRepositoryImpl implements TicketCustomRepository {

    private final JPAQueryFactory query;

    @Override
    public Optional<Ticket> findTicketByUserIdAndCafeIdAndInUseStatus(Long userId, Long cafeId) {
        return Optional.ofNullable(query
                .selectFrom(QTicket.ticket)
                .where(QTicket.ticket.userId.eq(userId),
                        QTicket.ticket.cafeId.eq(cafeId),
                        QTicket.ticket.status.eq(TicketStatus.IN_USE))
                .fetchOne());
    }

    @Override
    public Long updateAllValidTicketState() {
        return query
                .update(QTicket.ticket)
                .set(QTicket.ticket.status, TicketStatus.END_OF_USE)
                .where(QTicket.ticket.status.eq(TicketStatus.IN_USE),
                        QTicket.ticket.productLabel.contains("FIXED-TERM"),
                        QTicket.ticket.expiryDate.before(LocalDate.now()))
                .execute();
    }
}


