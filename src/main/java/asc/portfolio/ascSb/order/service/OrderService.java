package asc.portfolio.ascSb.order.service;

import asc.portfolio.ascSb.order.domain.OrderFinder;
import asc.portfolio.ascSb.order.domain.OrderStatus;
import asc.portfolio.ascSb.order.domain.Orders;
import asc.portfolio.ascSb.order.domain.OrdersRepository;
import asc.portfolio.ascSb.order.dto.OrderRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class OrderService {

    private final OrdersRepository ordersRepository;
    private final OrderFinder orderFinder;

    @Transactional
    public void saveOrder(Long userId, OrderRequest dto) {

        Orders orders = Orders.builder()
                .status(OrderStatus.PROCESSING)
                .userId(userId)
                .productLabel(dto.getProductLabel())
                .productType(dto.getProductType())
                .orderPrice(dto.getOrderPrice())
                .receiptId(dto.getReceiptId())
                .build();

        Orders saveOrders = ordersRepository.save(orders);
        log.info("주문번호={}", saveOrders.getId());
    }

    // todo 왜 엔티티 넘기고 있지...
    @Transactional(readOnly = true)
    public Orders findOrderByReceiptId(String receiptId) {
        return orderFinder.findByReceiptId(receiptId);
    }

    // todo 왜 엔티티 넘기고 있지...
    @Transactional(readOnly = true)
    public Orders findReceiptIdToProductLabel(String productLabel) {
        return orderFinder.findByProductLabel(productLabel);
    }
}
