package asc.portfolio.ascSb.domain.ticket;

import asc.portfolio.ascSb.web.dto.ticket.TicketSelectResponseDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import static asc.portfolio.ascSb.domain.ticket.QTicket.ticket;

@Repository
@RequiredArgsConstructor
public class TicketCustomRepositoryImpl implements TicketCustomRepository {

    private final JPAQueryFactory query;

    @Override
    public List<TicketSelectResponseDto> findAvailableTicketInfoById(Long id) {

        return query
                .select(Projections.bean(TicketSelectResponseDto.class,
                ticket.isDeprecatedTicket, ticket.fixedTermTicket, ticket.partTimeTicket, ticket.remainingTime))
                .from(ticket)
                .where(ticket.user.id.eq(id), ticket.isDeprecatedTicket.contains("N"))
                .fetch();
    }

    @Override
    public Long verifyTicket() {
        LocalDateTime date = LocalDateTime.now();

        return query
                .update(ticket)
                .set(ticket.isDeprecatedTicket, "Y")
                .where(ticket.fixedTermTicket.lt(date))
                .execute();
    }
}