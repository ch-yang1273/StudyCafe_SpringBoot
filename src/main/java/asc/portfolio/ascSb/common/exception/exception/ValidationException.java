package asc.portfolio.ascSb.common.exception.exception;

import asc.portfolio.ascSb.common.exception.dto.InvalidResponse;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ValidationException extends RuntimeException {

    List<InvalidResponse> responses;

    public ValidationException(List<InvalidResponse> list) {
        this.responses = new ArrayList<>();
        this.responses.addAll(list);
    }
}
