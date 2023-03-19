package asc.portfolio.ascSb.common.exception;

import asc.portfolio.ascSb.common.exception.dto.ExceptionResponse;
import asc.portfolio.ascSb.common.exception.exception.BusinessException;
import asc.portfolio.ascSb.common.exception.exception.ErrorData;
import asc.portfolio.ascSb.common.exception.exception.GlobalErrorData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class GlobalControllerAdvice {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ExceptionResponse> handleException(BusinessException e) {
        return convert(e);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ExceptionResponse> handleUnexpectedException(RuntimeException e) {
        log.error("Unexpected Exception", e);
        return convert(GlobalErrorData.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<ExceptionResponse> convert(BusinessException e) {
        int statusCode = e.getStatusCode();
        ExceptionResponse response = new ExceptionResponse(e.getErrorCode(), e.getMessage());
        return ResponseEntity.status(statusCode).body(response);
    }

    private ResponseEntity<ExceptionResponse> convert(ErrorData data) {
        int statusCode = data.getStatusCode();
        ExceptionResponse response = new ExceptionResponse(data.getErrorCode(), data.getMessage());
        return ResponseEntity.status(statusCode).body(response);
    }
}
