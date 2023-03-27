package asc.portfolio.ascSb.ticket.exception;

import asc.portfolio.ascSb.common.exception.exception.ErrorData;

public enum TicketErrorData implements ErrorData {
    TICKET_NOT_FOUND(400, "SEAT_4001", "존재하지 않는 좌석입니다.")
    ;

    private final int statusCode;
    private final String errorCode;
    private final String message;

    TicketErrorData(int statusCode, String errorCode, String message) {
        this.statusCode = statusCode;
        this.errorCode = errorCode;
        this.message = message;
    }

    @Override
    public int getStatusCode() {
        return statusCode;
    }

    @Override
    public String getErrorCode() {
        return errorCode;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
