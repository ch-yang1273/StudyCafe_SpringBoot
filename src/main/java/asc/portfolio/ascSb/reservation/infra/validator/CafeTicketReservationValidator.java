package asc.portfolio.ascSb.reservation.infra.validator;

import asc.portfolio.ascSb.cafe.domain.Cafe;
import asc.portfolio.ascSb.common.exception.dto.InvalidResponse;
import asc.portfolio.ascSb.reservation.domain.ReservationValidator;
import asc.portfolio.ascSb.reservation.dto.ValidationTarget;
import asc.portfolio.ascSb.ticket.domain.Ticket;
import asc.portfolio.ascSb.ticket.domain.TicketStateType;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class CafeTicketReservationValidator implements ReservationValidator {

    @Override
    public List<InvalidResponse> validate(ValidationTarget request) {
        Ticket ticket = request.getTicket();
        Cafe cafe = request.getCafe();

        if (ticket.getIsValidTicket() == TicketStateType.INVALID) {
            return Collections.singletonList(new InvalidResponse(request.getField(), "사용 할 수 없는 티켓입니다."));
        }

        if (!ticket.getCafe().equals(cafe)) {
            return Collections.singletonList(new InvalidResponse(request.getField(), "이 카페에서 사용 불가능한 티켓입니다."));
        }

        return Collections.emptyList();
    }
}
