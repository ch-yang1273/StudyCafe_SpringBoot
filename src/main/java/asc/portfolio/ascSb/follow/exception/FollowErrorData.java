package asc.portfolio.ascSb.follow.exception;

import asc.portfolio.ascSb.common.exception.exception.ErrorData;

public enum FollowErrorData implements ErrorData {
    NO_FOLLOWED_CAFE(400, "CAFE_4001", "follow 한 카페가 없습니다."),
    ;

    private final int statusCode;
    private final String errorCode;
    private final String message;

    FollowErrorData(int statusCode, String errorCode, String message) {
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
