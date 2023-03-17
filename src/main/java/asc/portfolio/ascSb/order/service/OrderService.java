package asc.portfolio.ascSb.order.service;

import asc.portfolio.ascSb.order.domain.Orders;
import asc.portfolio.ascSb.order.dto.OrderDto;

public interface OrderService {

    Long saveOrder(Long userId, OrderDto orderDto);

    Orders findReceiptOrderId(String receiptId);

    Orders findReceiptIdToProductLabel(String productLabel);

}
