package asc.portfolio.ascSb.order.exception;

import asc.portfolio.ascSb.common.exception.exception.BusinessException;

public class OrdersException extends BusinessException {
    public OrdersException(OrderErrorData data) {
        super(data);
    }
}
