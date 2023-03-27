package asc.portfolio.ascSb.ticket.exception;

import asc.portfolio.ascSb.common.exception.exception.BusinessException;
import asc.portfolio.ascSb.common.exception.exception.ErrorData;

public class TicketException extends BusinessException {

    public TicketException(ErrorData data) {
        super(data);
    }
}
