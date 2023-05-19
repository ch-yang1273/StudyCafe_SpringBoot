package asc.portfolio.ascSb.product.exception;

import asc.portfolio.ascSb.common.exception.exception.ErrorData;

public enum ProductErrorData implements ErrorData {
    PRODUCT_NOT_FOUND(400, "PRODUCT_001", "존재하지 않는 Product입니다.")
    ;
    private final int statusCode;
    private final String errorCode;
    private final String message;

    ProductErrorData(int statusCode, String errorCode, String message) {
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
