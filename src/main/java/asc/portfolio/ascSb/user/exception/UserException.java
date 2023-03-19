package asc.portfolio.ascSb.user.exception;

import asc.portfolio.ascSb.common.exception.exception.BusinessException;

public class UserException extends BusinessException {

    public UserException(UserErrorData data) {
        super(data);
    }
}
