package asc.portfolio.ascSb.reservation.exception;

import asc.portfolio.ascSb.common.exception.exception.ErrorData;

public enum ReservationErrorData implements ErrorData {
    RESERVATION_NOT_FOUND(400, "RESERVATION_4001", "존재하지 않는 Reservation입니다."),
    RESERVATION_NOT_IN_USE(400, "RESERVATION_4002", "사용 중인 Reservation이 없습니다."),

    VALIDATE_UNMATCHED_CAFE(400, "RESERVATION_4003", "카페에 속하지 않은 좌석입니다."),
    VALIDATE_ALREADY_HAS_SEAT(400, "RESERVATION_4004", "이미 사용 중인 좌석이 있습니다."),
    VALIDATE_NO_TARGET(400, "RESERVATION_4005", "Validation target이 없습니다."),
    ;

    private final int statusCode;
    private final String errorCode;
    private final String message;

    ReservationErrorData(int statusCode, String errorCode, String message) {
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
