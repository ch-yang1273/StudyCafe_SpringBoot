package asc.portfolio.ascSb.reservation.domain;

import asc.portfolio.ascSb.cafe.domain.Cafe;
import asc.portfolio.ascSb.cafe.domain.CafeFinder;
import asc.portfolio.ascSb.common.exception.dto.InvalidResponse;
import asc.portfolio.ascSb.common.exception.exception.ValidationException;
import asc.portfolio.ascSb.reservation.dto.ValidationTarget;
import asc.portfolio.ascSb.reservation.exception.ReservationErrorData;
import asc.portfolio.ascSb.reservation.exception.ReservationException;
import asc.portfolio.ascSb.seat.domain.Seat;
import asc.portfolio.ascSb.seat.domain.SeatFinder;
import asc.portfolio.ascSb.ticket.domain.Ticket;
import asc.portfolio.ascSb.ticket.domain.TicketFinder;
import asc.portfolio.ascSb.user.domain.User;
import asc.portfolio.ascSb.user.domain.UserFinder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class ReservationValidation {

    private final List<ReservationValidator> validators;

    UserFinder userFinder;
    CafeFinder cafeFinder;
    SeatFinder seatFinder;
    TicketFinder ticketFinder;

    private ValidationTarget createValidationTarget(Reservation rez) {
        User user = userFinder.findById(rez.getUserId());
        Cafe cafe = cafeFinder.findById(rez.getCafeId());
        Seat seat = seatFinder.findById(rez.getSeatId());
        Ticket ticket = ticketFinder.findById(rez.getTicketId());

        return ValidationTarget.builder()
                .user(user)
                .cafe(cafe)
                .seat(seat)
                .ticket(ticket)
                .build();
    }

    public void validate(Reservation rez) {
        if (rez == null) {
            throw new ReservationException(ReservationErrorData.VALIDATE_NO_TARGET);
        }
        ValidationTarget target = createValidationTarget(rez);

        List<InvalidResponse> list = validators.stream()
                .map(validator -> validator.validate(target))
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

        if (!list.isEmpty()) {
            throw new ValidationException(list);
        }
    }
}
