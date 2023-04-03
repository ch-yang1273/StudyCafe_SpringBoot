package asc.portfolio.ascSb.ticket.domain;

import java.util.Optional;

public interface TicketCustomRepository {

    Optional<Ticket> findTicketByUserIdAndCafeIdAndInUseStatus(Long userId, Long cafeId);
}