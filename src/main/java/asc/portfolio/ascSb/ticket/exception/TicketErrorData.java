package asc.portfolio.ascSb.ticket.exception;

import asc.portfolio.ascSb.common.exception.exception.ErrorData;

public enum TicketErrorData implements ErrorData {
    TICKET_NOT_FOUND(400, "SEAT_4001", "존재하지 않는 좌석입니다."),
    CANNOT_EXTEND_DIFFERENT_TYPE(400, "SEAT_4002", "다른 타입으로는 티켓을 연장할 수 없습니다."),
    NOT_PART_TIME_TICKET(400, "TICKET_4003", "PartTime Ticket이 아닙니다."),
    NOT_FIXED_TERM_TICKET(400, "TICKET_4004", "FixedTerm Ticket이 아닙니다."),
    TICKET_NOT_USABLE(400, "TICKET_4005", "사용 가능한 티켓이 아닙니다.")
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
