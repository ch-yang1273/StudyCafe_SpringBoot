package asc.portfolio.ascSb.order.exception;

import asc.portfolio.ascSb.common.exception.exception.ErrorData;

public enum OrderErrorData implements ErrorData {
    ORDER_NOT_FOUND(400, "ORDERS_001", "존재하지 않는 Order입니다.")
    ;
    private final int statusCode;
    private final String errorCode;
    private final String message;

    OrderErrorData(int statusCode, String errorCode, String message) {
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
