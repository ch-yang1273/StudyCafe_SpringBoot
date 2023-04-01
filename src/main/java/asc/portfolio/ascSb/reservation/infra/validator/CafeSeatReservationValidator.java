package asc.portfolio.ascSb.reservation.infra.validator;

import asc.portfolio.ascSb.cafe.domain.Cafe;
import asc.portfolio.ascSb.common.exception.dto.InvalidResponse;
import asc.portfolio.ascSb.reservation.domain.ReservationValidator;
import asc.portfolio.ascSb.reservation.dto.ValidationTarget;
import asc.portfolio.ascSb.seat.domain.Seat;
import asc.portfolio.ascSb.seat.domain.SeatUsageStatus;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class CafeSeatReservationValidator implements ReservationValidator {

    // User가 이미 사용 중인 좌석이 있는지?
    @Override
    public List<InvalidResponse> validate(ValidationTarget request) {
        Cafe cafe = request.getCafe();
        Seat seat = request.getSeat();

        if (!seat.isBelongTo(cafe.getId())) {
            return Collections.singletonList(new InvalidResponse(request.getField(), "카페에 매칭되지 않는 좌석입니다."));
        }

        if (!cafe.isOpen()) {
            return Collections.singletonList(new InvalidResponse(request.getField(), "카페가 영업 중이 아닙니다."));
        }

        if (seat.getUsageStatus() == SeatUsageStatus.IN_USE) {
            return Collections.singletonList(new InvalidResponse(request.getField(), "사용 중인 좌석입니다.."));
        }

        return Collections.emptyList();
    }
}
