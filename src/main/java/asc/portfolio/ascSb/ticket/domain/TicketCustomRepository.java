package asc.portfolio.ascSb.ticket.domain;

import java.util.Optional;

public interface TicketCustomRepository {

    Optional<Ticket> findTicketByUserIdAndCafeIdAndTicketStatus(Long userId, Long cafeId, TicketStatus status);
}