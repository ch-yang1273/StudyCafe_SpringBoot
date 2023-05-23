package asc.portfolio.ascSb.push.exception;

import asc.portfolio.ascSb.common.exception.exception.BusinessException;

public class PushException extends BusinessException {

    public PushException(PushErrorData data) {
        super(data);
    }
}
