package asc.portfolio.ascSb.common.exception.exception;

import asc.portfolio.ascSb.common.exception.dto.ValidationResponse;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class RuleViolationException extends RuntimeException {

    List<ValidationResponse> responses;

    public RuleViolationException(List<ValidationResponse> list) {
        this.responses = new ArrayList<>();
        this.responses.addAll(list);
    }
}
