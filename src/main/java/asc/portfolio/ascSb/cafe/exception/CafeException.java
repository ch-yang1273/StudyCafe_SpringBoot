package asc.portfolio.ascSb.cafe.exception;

import asc.portfolio.ascSb.common.exception.exception.BusinessException;
import asc.portfolio.ascSb.common.exception.exception.ErrorData;

public class CafeException extends BusinessException {

    public CafeException(ErrorData data) {
        super(data);
    }
}
