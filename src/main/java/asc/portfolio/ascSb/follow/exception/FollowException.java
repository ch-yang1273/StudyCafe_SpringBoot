package asc.portfolio.ascSb.follow.exception;

import asc.portfolio.ascSb.common.exception.exception.BusinessException;
import asc.portfolio.ascSb.common.exception.exception.ErrorData;

public class FollowException extends BusinessException {

    public FollowException(ErrorData data) {
        super(data);
    }
}
