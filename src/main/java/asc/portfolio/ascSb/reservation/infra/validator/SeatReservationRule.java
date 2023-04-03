package asc.portfolio.ascSb.reservation.infra.validator;

import asc.portfolio.ascSb.common.exception.dto.ValidationResponse;
import asc.portfolio.ascSb.reservation.domain.ReservationRule;
import asc.portfolio.ascSb.reservation.dto.ValidationContext;
import asc.portfolio.ascSb.seat.domain.Seat;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class SeatReservationRule implements ReservationRule {

    @Override
    public List<ValidationResponse> validate(ValidationContext request) {
        Seat seat = request.getSeat();

        List<ValidationResponse> list = new ArrayList<>();

        if (!seat.canReserve()) {
            list.add(new ValidationResponse(request.getField(), "이미 예약된 좌석입니다."));
            log.debug("seat.canReserve() = {}, message = {}", seat.canReserve(), "이미 예약된 좌석입니다.");
        }

        return list;
    }
}
