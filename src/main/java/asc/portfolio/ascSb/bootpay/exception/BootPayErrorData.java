package asc.portfolio.ascSb.bootpay.exception;

import asc.portfolio.ascSb.common.exception.exception.ErrorData;

public enum BootPayErrorData implements ErrorData {
    RECEIPT_LOOKUP_ERROR(400, "PAY_4001", "Bootpay Receipt 조회 에러입니다."),

    TOKEN_REFRESH_ERROR(500, "PAY_5001", "Bootpay token 갱신 에러입니다."),
    RECEIPT_CONFIRM_ERROR(500, "PAY_5002", "Bootpay Receipt 결제 승인 에러입니다."),
    RECEIPT_CANCEL_ERROR(500, "PAY_5003", "Bootpay Receipt 결제 취소 에러입니다.")
    ;

    private final int statusCode;
    private final String errorCode;
    private final String message;

    BootPayErrorData(int statusCode, String errorCode, String message) {
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
