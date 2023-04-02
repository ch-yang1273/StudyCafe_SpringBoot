package asc.portfolio.ascSb.reservation.dto;

import asc.portfolio.ascSb.cafe.domain.Cafe;
import asc.portfolio.ascSb.reservation.domain.Reservation;
import asc.portfolio.ascSb.seat.domain.Seat;
import asc.portfolio.ascSb.ticket.domain.Ticket;
import asc.portfolio.ascSb.user.domain.User;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ValidationContext {

    private final String field = "Reservation";
    private final Reservation reservation;
    private final User user;
    private final Cafe cafe;
    private final Seat seat;
    private final Ticket ticket;

    @Builder
    public ValidationContext(Reservation reservation, User user, Cafe cafe, Seat seat, Ticket ticket) {
        this.reservation = reservation;
        this.user = user;
        this.cafe = cafe;
        this.seat = seat;
        this.ticket = ticket;
    }
}
