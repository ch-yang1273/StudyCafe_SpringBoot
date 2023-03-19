package asc.portfolio.ascSb.user.exception;

import asc.portfolio.ascSb.common.exception.exception.ErrorData;

public enum UserErrorData implements ErrorData {
    USER_NOT_FOUND(400, "USER_4001", "존재하지 않는 유저입니다."),
    USER_WRONG_PASSWORD(400, "USER_4002", "비밀번호가 일치하지 않습니다."),

    USER_NO_TOKEN(401, "USER_4003", "토큰이 없습니다."),
    USER_WRONG_TOKEN(401, "USER_4004", "올바르지 않은 토큰입니다."),
    USER_EXPIRED_TOKEN(401, "USER_4005", "만료된 토큰입니다.")
    ;

    private final int statusCode;
    private final String errorCode;
    private final String message;

    UserErrorData(int statusCode, String errorCode, String message) {
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
