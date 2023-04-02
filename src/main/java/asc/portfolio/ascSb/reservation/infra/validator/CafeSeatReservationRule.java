package asc.portfolio.ascSb.reservation.infra.validator;

import asc.portfolio.ascSb.cafe.domain.Cafe;
import asc.portfolio.ascSb.common.exception.dto.ValidationResponse;
import asc.portfolio.ascSb.reservation.domain.ReservationRule;
import asc.portfolio.ascSb.reservation.dto.ValidationContext;
import asc.portfolio.ascSb.seat.domain.Seat;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class CafeSeatReservationRule implements ReservationRule {

    @Override
    public List<ValidationResponse> validate(ValidationContext request) {
        Cafe cafe = request.getCafe();
        Seat seat = request.getSeat();

        if (!seat.isBelongTo(cafe.getId())) {
            return Collections.singletonList(new ValidationResponse(request.getField(), "카페에 매칭되지 않는 좌석입니다."));
        }

        if (!cafe.isOpen()) {
            return Collections.singletonList(new ValidationResponse(request.getField(), "카페가 영업 중이 아닙니다."));
        }

        if (!seat.canReserve()) {
            return Collections.singletonList(new ValidationResponse(request.getField(), "사용 중인 좌석입니다.."));
        }

        return Collections.emptyList();
    }
}
