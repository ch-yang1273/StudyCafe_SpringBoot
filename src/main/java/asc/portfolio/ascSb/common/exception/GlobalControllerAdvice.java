package asc.portfolio.ascSb.common.exception;

import asc.portfolio.ascSb.common.exception.dto.ExceptionResponse;
import asc.portfolio.ascSb.common.exception.dto.ValidationResponse;
import asc.portfolio.ascSb.common.exception.exception.BusinessException;
import asc.portfolio.ascSb.common.exception.exception.ErrorData;
import asc.portfolio.ascSb.common.exception.exception.GlobalErrorData;
import asc.portfolio.ascSb.common.exception.exception.RuleViolationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ExceptionResponse> handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        return convert(GlobalErrorData.UNIQUE_VIOLATION);
    }

    @ExceptionHandler(RuleViolationException.class)
    public ResponseEntity<List<ValidationResponse>> handleRuleViolationException(RuleViolationException e) {
        return convert(e);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) throws JsonProcessingException {
        BindingResult bindingResult = e.getBindingResult();

        Map<String, String> map = new HashMap<>();
        for (FieldError error : bindingResult.getFieldErrors()) {
            map.put(error.getField(), error.getDefaultMessage());
        }

        ObjectMapper mapper = new ObjectMapper();
        return ResponseEntity.badRequest().body(mapper.writeValueAsString(map));
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
