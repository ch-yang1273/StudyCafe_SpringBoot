package asc.portfolio.ascSb.reservation.infra.validator;

import asc.portfolio.ascSb.common.exception.dto.InvalidResponse;
import asc.portfolio.ascSb.reservation.domain.ReservationValidator;
import asc.portfolio.ascSb.reservation.dto.ValidationTarget;
import asc.portfolio.ascSb.seat.domain.SeatFinder;
import asc.portfolio.ascSb.user.domain.User;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class UserReservationValidator implements ReservationValidator {

    private final SeatFinder seatFinder;

    public UserReservationValidator(SeatFinder seatFinder) {
        this.seatFinder = seatFinder;
    }

    // User가 이미 사용 중인 좌석이 있는지?
    @Override
    public List<InvalidResponse> validate(ValidationTarget request) {
        User user = request.getUser();

        if (seatFinder.hasReservedSeat(user.getId())) {
            return Collections.singletonList(new InvalidResponse(request.getField(), "이미 예약한 좌석이 있습니다."));
        }

        return Collections.emptyList();
    }
}
