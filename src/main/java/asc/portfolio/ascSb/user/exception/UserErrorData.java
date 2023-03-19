package asc.portfolio.ascSb.user.exception;

import asc.portfolio.ascSb.common.exception.exception.ErrorData;

public enum UserErrorData implements ErrorData {
    USER_NOT_FOUND(400, "USER_4001", "존재하지 않는 유저입니다."),
    USER_WRONG_PASSWORD(400, "USER_4002", "비밀번호가 일치하지 않습니다.")
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
