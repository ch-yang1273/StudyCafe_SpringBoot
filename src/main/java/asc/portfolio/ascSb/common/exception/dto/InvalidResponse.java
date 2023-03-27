package asc.portfolio.ascSb.common.exception.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class InvalidResponse {

    private final String field;
    private final String message;
}
