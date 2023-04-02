package asc.portfolio.ascSb.support.ticket;

import asc.portfolio.ascSb.cafe.domain.Cafe;
import asc.portfolio.ascSb.ticket.domain.Ticket;
import asc.portfolio.ascSb.ticket.domain.TicketStatus;
import asc.portfolio.ascSb.ticket.domain.TicketType;
import asc.portfolio.ascSb.user.domain.User;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public enum TicketFixture {

    TICKET_A(1L, TicketStatus.IN_USE),
    TICKET_B(2L, TicketStatus.IN_USE),
    TICKET_C(3L, TicketStatus.IN_USE),
    TICKET_D(4L, TicketStatus.END_OF_USE)
    ;

    private final Long id;
    private final TicketStatus status;
    private final Integer price;

    TicketFixture(Long id, TicketStatus status) {
        this.id = id;
        this.status = status;
        this.price = 1000;
    }

    public Ticket toFixedTermTicket(User user, Cafe cafe, LocalDate expiryDate) {
        return Ticket.builder()
                .id(id)
                .userId(user.getId())
                .cafeId(cafe.getId())
                .ticketType(TicketType.FIXED_TERM)
                .status(status)
                .price(price)
                .expiryDate(expiryDate)
                .totalDuration(null)
                .remainMinute(null)
                .build();
    }

    public Ticket toPartTimeTicket(User user, Cafe cafe, Long totalDuration) {
        return Ticket.builder()
                .id(id)
                .userId(user.getId())
                .cafeId(cafe.getId())
                .ticketType(TicketType.PART_TERM)
                .status(status)
                .price(price)
                .expiryDate(null)
                .totalDuration(totalDuration)
                .remainMinute(totalDuration)
                .build();
    }
}
