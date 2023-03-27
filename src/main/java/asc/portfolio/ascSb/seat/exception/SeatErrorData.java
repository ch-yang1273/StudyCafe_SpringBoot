package asc.portfolio.ascSb.seat.exception;

import asc.portfolio.ascSb.common.exception.exception.ErrorData;

public enum SeatErrorData implements ErrorData {
    SEAT_NOT_FOUND(400, "SEAT_4001", "존재하지 않는 좌석입니다."),

    HAS_ALREADY_IN_USE(400, "SEAT_4002", "이미 사용 중인 좌석이 있습니다."),
    NOT_MATCHED_SEAT_CAFE(400, "SEAT_4003", "카페에 속하지 않는 좌석입니다.")
    ;

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
