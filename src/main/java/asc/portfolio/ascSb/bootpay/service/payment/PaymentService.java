package asc.portfolio.ascSb.bootpay.service.payment;

import asc.portfolio.ascSb.order.domain.Orders;
import asc.portfolio.ascSb.bootpay.dto.BootPayOrderDto;

public interface PaymentService {

    boolean modifyAndAddValidPayment(Orders orders, Long userId, BootPayOrderDto dto);
}
