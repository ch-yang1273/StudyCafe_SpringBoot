package asc.portfolio.ascSb.push.exception;

import asc.portfolio.ascSb.common.exception.exception.ErrorData;

public enum PushErrorData implements ErrorData {
    TOKEN_NOT_FOUND(400, "FCM_4001", "등록 된 Device Token이 없습니다."),
    ;

    private final int statusCode;
    private final String errorCode;
    private final String message;

    PushErrorData(int statusCode, String errorCode, String message) {
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
