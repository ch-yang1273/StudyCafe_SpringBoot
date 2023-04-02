package asc.portfolio.ascSb.reservation.infra.validator;

import asc.portfolio.ascSb.cafe.domain.Cafe;
import asc.portfolio.ascSb.common.exception.dto.ValidationResponse;
import asc.portfolio.ascSb.reservation.domain.ReservationRule;
import asc.portfolio.ascSb.reservation.dto.ValidationContext;
import asc.portfolio.ascSb.ticket.domain.Ticket;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class CafeTicketReservationRule implements ReservationRule {

    @Override
    public List<ValidationResponse> validate(ValidationContext request) {
        Ticket ticket = request.getTicket();
        Cafe cafe = request.getCafe();

        if (ticket.isTicketUsable()) {
            return Collections.singletonList(new ValidationResponse(request.getField(), "사용 할 수 없는 티켓입니다."));
        }

        if (!ticket.getCafeId().equals(cafe.getId())) {
            return Collections.singletonList(new ValidationResponse(request.getField(), "이 카페에서 사용 불가능한 티켓입니다."));
        }

        return Collections.emptyList();
    }
}
