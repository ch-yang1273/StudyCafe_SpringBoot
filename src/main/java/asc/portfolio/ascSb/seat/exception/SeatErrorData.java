package asc.portfolio.ascSb.seat.exception;

import asc.portfolio.ascSb.common.exception.exception.ErrorData;

public enum SeatErrorData implements ErrorData {
    SEAT_NOT_FOUND(400, "SEAT_4001", "존재하지 않는 좌석입니다."),

    NOT_MATCHED_SEAT_CAFE(400, "SEAT_4002", "카페에 속하지 않는 좌석입니다."),
    NOT_AVAILABLE_SEAT(400, "SEAT_4003", "사용 가능한 좌석이 아닙니다.");


    private final int statusCode;
    private final String errorCode;
    private final String message;

    SeatErrorData(int statusCode, String errorCode, String message) {
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
