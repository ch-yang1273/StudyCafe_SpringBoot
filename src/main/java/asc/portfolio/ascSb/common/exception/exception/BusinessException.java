package asc.portfolio.ascSb.common.exception.exception;

import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {

    private final int statusCode;
    private final String errorCode;
    private final String message;

    public BusinessException(ErrorData data) {
        statusCode = data.getStatusCode();
        errorCode = data.getErrorCode();
        message = data.getMessage();
    }
}
