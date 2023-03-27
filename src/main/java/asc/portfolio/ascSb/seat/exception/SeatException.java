package asc.portfolio.ascSb.seat.exception;

import asc.portfolio.ascSb.common.exception.exception.BusinessException;

public class SeatException extends BusinessException {

    public SeatException(SeatErrorData data) {
        super(data);
    }
}
