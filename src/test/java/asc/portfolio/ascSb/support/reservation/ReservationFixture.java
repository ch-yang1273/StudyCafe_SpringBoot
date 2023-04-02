package asc.portfolio.ascSb.support.reservation;

import asc.portfolio.ascSb.cafe.domain.Cafe;
import asc.portfolio.ascSb.reservation.domain.Reservation;
import asc.portfolio.ascSb.seat.domain.Seat;
import asc.portfolio.ascSb.ticket.domain.Ticket;
import asc.portfolio.ascSb.user.domain.User;

import java.time.LocalDateTime;

public enum ReservationFixture {
    RESERVATION_A(1L),
    RESERVATION_B(2L),
    RESERVATION_C(3L),
    RESERVATION_D(4L)
    ;
    private final Long id;

    ReservationFixture(Long id) {
        this.id = id;
    }

    public Reservation toReservation(User user, Cafe cafe, Seat seat, Ticket ticket, LocalDateTime startTime) {
        return Reservation.builder()
                .id(id)
                .userId(user.getId())
                .cafeId(cafe.getId())
                .seatId(seat.getId())
                .ticketId(ticket.getId())
                .startTime(startTime)
                .build();
    }
}
