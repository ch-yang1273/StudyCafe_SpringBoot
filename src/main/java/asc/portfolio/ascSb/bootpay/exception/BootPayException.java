package asc.portfolio.ascSb.bootpay.exception;

import asc.portfolio.ascSb.common.exception.exception.BusinessException;

public class BootPayException extends BusinessException {

    public BootPayException(BootPayErrorData data) {
        super(data);
    }
}
