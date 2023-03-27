package asc.portfolio.ascSb.reservation.exception;

import asc.portfolio.ascSb.common.exception.exception.BusinessException;
import asc.portfolio.ascSb.common.exception.exception.ErrorData;

public class ReservationException extends BusinessException {
    public ReservationException(ErrorData data) {
        super(data);
    }
}
