package asc.portfolio.ascSb.common.exception.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum GlobalErrorData implements ErrorData {

    INTERNAL_SERVER_ERROR(HttpStatus.SC_INTERNAL_SERVER_ERROR, "SERVER_001", "내부 서버 오류입니다."),
    UNIQUE_VIOLATION(HttpStatus.SC_CONFLICT, "SERVER_002", "중복된 데이터입니다. 유니크 제약 조건이 위반되었습니다."),
    ;

    private final int statusCode;
    private final String errorCode;
    private final String message;
}
