package asc.portfolio.ascSb.reservation.domain;

import asc.portfolio.ascSb.cafe.domain.Cafe;
import asc.portfolio.ascSb.common.exception.dto.ValidationResponse;
import asc.portfolio.ascSb.common.exception.exception.RuleViolationException;
import asc.portfolio.ascSb.reservation.dto.ValidationContext;
import asc.portfolio.ascSb.seat.domain.Seat;
import asc.portfolio.ascSb.ticket.domain.Ticket;
import asc.portfolio.ascSb.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class ReservationValidator {

    private final List<ReservationRule> reservationRules;

    public void validate(User user, Cafe cafe, Seat seat, Ticket ticket) {
        ValidationContext target = new ValidationContext(user, cafe, seat, ticket);

        List<ValidationResponse> list = reservationRules.stream()
                .map(rule -> rule.validate(target))
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

        if (!list.isEmpty()) {
            throw new RuleViolationException(list);
        }
    }
}
