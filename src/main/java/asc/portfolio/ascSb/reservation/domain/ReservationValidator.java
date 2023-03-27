package asc.portfolio.ascSb.reservation.domain;

import asc.portfolio.ascSb.common.exception.dto.InvalidResponse;
import asc.portfolio.ascSb.reservation.dto.ValidationTarget;

import java.util.List;

public interface ReservationValidator {

    List<InvalidResponse> validate(ValidationTarget request);
}
