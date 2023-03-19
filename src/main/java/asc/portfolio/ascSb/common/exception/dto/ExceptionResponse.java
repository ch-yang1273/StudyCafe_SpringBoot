package asc.portfolio.ascSb.common.exception.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ExceptionResponse {

    private String code;
    private String message;
}
