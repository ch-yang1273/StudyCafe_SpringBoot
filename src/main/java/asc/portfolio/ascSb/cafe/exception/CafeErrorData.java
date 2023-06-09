package asc.portfolio.ascSb.cafe.exception;

import asc.portfolio.ascSb.common.exception.exception.ErrorData;

public enum CafeErrorData implements ErrorData {
    CAFE_NOT_FOUND(400, "CAFE_4001", "존재하지 않는 카페입니다."),
    UNMATCHED_ADMIN(401, "CAFE_4002", "이 카페의 관리자가 아닙니다."),
    ADMIN_NOT_FOUND(400, "CAFE_4003", "존재하지 않는 관리자입니다."),

    CAFE_NEED_ADMIN_ROLE(401, "CAFE_4004", "Admin Role이 필요합니다."),
    CAFE_NEED_AUTH(401, "CAFE_4005", "권한이 없습니다.")
    ;

    private final int statusCode;
    private final String errorCode;
    private final String message;

    CafeErrorData(int statusCode, String errorCode, String message) {
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
