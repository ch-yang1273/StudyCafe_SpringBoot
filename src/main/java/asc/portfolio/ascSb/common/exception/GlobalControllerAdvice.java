package asc.portfolio.ascSb.common.exception;

import asc.portfolio.ascSb.common.exception.dto.ExceptionResponse;
import asc.portfolio.ascSb.common.exception.dto.ValidationResponse;
import asc.portfolio.ascSb.common.exception.exception.BusinessException;
import asc.portfolio.ascSb.common.exception.exception.ErrorData;
import asc.portfolio.ascSb.common.exception.exception.GlobalErrorData;
import asc.portfolio.ascSb.common.exception.exception.RuleViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;

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

    @ExceptionHandler(RuleViolationException.class)
    public ResponseEntity<List<ValidationResponse>> handleRuleViolationException(RuleViolationException e) {
        return convert(e);
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

    private ResponseEntity<List<ValidationResponse>> convert(RuleViolationException e) {
        List<ValidationResponse> responses = e.getResponses();
        for (ValidationResponse response : responses) {
            log.debug("field = {}, message = {}", response.getField(), response.getMessage());
        }
        return ResponseEntity.status(400).body(responses);
    }
}
