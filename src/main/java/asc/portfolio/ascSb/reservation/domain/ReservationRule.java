package asc.portfolio.ascSb.reservation.domain;

import asc.portfolio.ascSb.common.exception.dto.ValidationResponse;
import asc.portfolio.ascSb.reservation.dto.ValidationContext;

import java.util.List;

public interface ReservationRule {

    List<ValidationResponse> validate(ValidationContext request);
}
