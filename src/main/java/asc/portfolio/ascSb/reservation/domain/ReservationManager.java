package asc.portfolio.ascSb.reservation.domain;

import asc.portfolio.ascSb.cafe.domain.Cafe;
import asc.portfolio.ascSb.cafe.domain.CafeFinder;
import asc.portfolio.ascSb.common.domain.CurrentTimeProvider;
import asc.portfolio.ascSb.seat.domain.Seat;
import asc.portfolio.ascSb.seat.domain.SeatFinder;
import asc.portfolio.ascSb.ticket.domain.Ticket;
import asc.portfolio.ascSb.ticket.domain.TicketFinder;
import asc.portfolio.ascSb.user.domain.User;
import asc.portfolio.ascSb.user.domain.UserFinder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ReservationManager {

    private final ReservationRepository reservationRepository;
    private final UserFinder userFinder;
    private final CafeFinder cafeFinder;
    private final SeatFinder seatFinder;
    private final TicketFinder ticketFinder;

    private final CurrentTimeProvider currentTimeProvider;

    public void reserve(Reservation reservation) {
        Seat seat = seatFinder.findById(reservation.getSeatId());
        Ticket ticket = ticketFinder.findById(reservation.getTicketId());

        seat.reserveSeat(reservation, ticket, currentTimeProvider.localDateTimeNow());

        reservationRepository.save(reservation);
    }

    public void release(Long authUserId, Reservation reservation) {
        User user = userFinder.findById(authUserId); // 자신 or 관리자
        Cafe cafe = cafeFinder.findByAdminId(reservation.getCafeId());
        Seat seat = seatFinder.findById(reservation.getSeatId());

        reservation.release(user, cafe, seat, currentTimeProvider.localDateTimeNow());
    }
}
