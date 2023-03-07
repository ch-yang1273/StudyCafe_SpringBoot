package asc.portfolio.ascSb.order.service;

import asc.portfolio.ascSb.order.domain.Orders;
import asc.portfolio.ascSb.user.domain.User;
import asc.portfolio.ascSb.order.dto.OrderDto;

public interface OrderService {

    Long saveOrder(User user, OrderDto orderDto);

    Orders findReceiptOrderId(String receiptId);

    Orders findReceiptIdToProductLabel(String productLabel);

}
