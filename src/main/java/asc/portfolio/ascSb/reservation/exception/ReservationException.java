package asc.portfolio.ascSb.reservation.exception;

import asc.portfolio.ascSb.common.exception.exception.BusinessException;

public class ReservationException extends BusinessException {
    public ReservationException(ReservationErrorData data) {
        super(data);
    }
}
