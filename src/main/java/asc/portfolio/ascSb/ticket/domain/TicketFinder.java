package asc.portfolio.ascSb.ticket.domain;

import asc.portfolio.ascSb.ticket.exception.TicketErrorData;
import asc.portfolio.ascSb.ticket.exception.TicketException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class TicketFinder {

    private final TicketRepository ticketRepository;

    public Ticket findById(Long ticketId) {
        return ticketRepository.findById(ticketId)
                .orElseThrow(() -> new TicketException(TicketErrorData.TICKET_NOT_FOUND));
    }

    public Ticket findTicketByUserIdAndCafeIdAndInUseStatus(Long userId, Long cafeId) {
        return ticketRepository.findTicketByUserIdAndCafeIdAndInUseStatus(userId, cafeId)
                .orElseThrow(() -> new TicketException(TicketErrorData.TICKET_NOT_FOUND));
    }

    public List<Ticket> findAllByUserIdAndCafeId(Long userId, Long cafeId) {
        return ticketRepository.findAllByUserIdAndCafeId(userId, cafeId);
    }

    public List<Ticket> findAllByUserId(Long userId) {
        return ticketRepository.findAllByUserId(userId);
    }
}
