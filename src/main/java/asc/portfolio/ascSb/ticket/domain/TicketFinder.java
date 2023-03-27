package asc.portfolio.ascSb.ticket.domain;

import asc.portfolio.ascSb.ticket.exception.TicketErrorData;
import asc.portfolio.ascSb.ticket.exception.TicketException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class TicketFinder {

    private final TicketRepository ticketRepository;

    public Ticket findById(Long ticketId) {
        return ticketRepository.findById(ticketId).orElseThrow(() -> new TicketException(TicketErrorData.TICKET_NOT_FOUND));
    }
}
