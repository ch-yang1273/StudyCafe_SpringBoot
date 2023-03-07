package asc.portfolio.ascSb.common.infra.payment;

import asc.portfolio.ascSb.order.domain.Orders;
import asc.portfolio.ascSb.user.domain.User;
import asc.portfolio.ascSb.common.infra.bootpay.dto.BootPayOrderDto;

public interface PaymentService {

    boolean modifyAndAddValidPayment(Orders orders, User user, BootPayOrderDto dto);
}
