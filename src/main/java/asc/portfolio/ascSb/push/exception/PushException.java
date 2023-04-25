package asc.portfolio.ascSb.push.exception;

import asc.portfolio.ascSb.common.exception.exception.BusinessException;
import asc.portfolio.ascSb.common.exception.exception.ErrorData;

public class PushException extends BusinessException {

    public PushException(ErrorData data) {
        super(data);
    }
}
