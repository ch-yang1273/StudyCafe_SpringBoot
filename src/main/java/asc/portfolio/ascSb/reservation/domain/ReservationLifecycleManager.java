package asc.portfolio.ascSb.reservation.domain;

import asc.portfolio.ascSb.cafe.domain.Cafe;
import asc.portfolio.ascSb.cafe.domain.CafeFinder;
import asc.portfolio.ascSb.seat.domain.Seat;
import asc.portfolio.ascSb.seat.domain.SeatFinder;
import asc.portfolio.ascSb.ticket.domain.Ticket;
import asc.portfolio.ascSb.ticket.domain.TicketFinder;
import asc.portfolio.ascSb.user.domain.User;
import asc.portfolio.ascSb.user.domain.UserFinder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Component
public class ReservationLifecycleManager {

    private final ReservationRepository reservationRepository;
    private final UserFinder userFinder;
    private final CafeFinder cafeFinder;
    private final SeatFinder seatFinder;
    private final TicketFinder ticketFinder;

    private final ReservationValidator reservationValidator;

    public void reserve(Long userId, Long cafeId, Long seatId, Long tickerId, LocalDateTime now) {
        User user = userFinder.findById(userId);
        Cafe cafe = cafeFinder.findById(cafeId);
        Seat seat = seatFinder.findById(seatId);
        Ticket ticket = ticketFinder.findById(tickerId);

        Reservation reservation = new Reservation(user, cafe, seat, ticket, now);
        reservationValidator.validate(reservation, user, cafe, seat, ticket);

        reservationRepository.save(reservation);
    }

    public void finish(Long authUserId, Reservation reservation, LocalDateTime now) {
        User user = userFinder.findById(authUserId); // 자신 or 관리자
        Cafe cafe = cafeFinder.findById(reservation.getCafeId());
        Seat seat = seatFinder.findById(reservation.getSeatId());
        Ticket ticket = ticketFinder.findById(reservation.getTicketId());

        reservation.finish(user, cafe, seat, ticket, now);
    }
}
