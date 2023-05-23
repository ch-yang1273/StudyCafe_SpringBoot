package asc.portfolio.ascSb.order.service;

import asc.portfolio.ascSb.order.domain.OrderFinder;
import asc.portfolio.ascSb.order.domain.OrderStatus;
import asc.portfolio.ascSb.order.domain.Orders;
import asc.portfolio.ascSb.order.domain.OrdersRepository;
import asc.portfolio.ascSb.order.dto.OrderRequest;
import asc.portfolio.ascSb.product.domain.Product;
import asc.portfolio.ascSb.product.domain.ProductFinder;
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

    // todo 삭제
    private final ProductFinder productFinder;

    @Transactional
    public void saveOrder(Long userId, OrderRequest dto) {

        Orders orders = Orders.builder()
                .status(OrderStatus.PROCESSING)
                .userId(userId)
                .productLabel(dto.getProductLabel())
                .productType(dto.getProductType())
                .price(dto.getPrice())
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
    public Orders findReceiptIdToProductLabel(Long productId) {
        Product product = productFinder.findById(productId);
        return orderFinder.findByProductLabel(product.getLabel());
    }
}
