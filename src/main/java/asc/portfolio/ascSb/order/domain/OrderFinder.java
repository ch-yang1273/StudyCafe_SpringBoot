package asc.portfolio.ascSb.order.domain;

import asc.portfolio.ascSb.order.exception.OrderErrorData;
import asc.portfolio.ascSb.order.exception.OrdersException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class OrderFinder {

    private final OrdersRepository ordersRepository;

    public Orders findByReceiptId(String receiptId) {
        return ordersRepository.findByReceiptId(receiptId).orElseThrow(
                () -> new OrdersException(OrderErrorData.ORDER_NOT_FOUND)
        );
    }

    public Orders findByProductLabel(String productLabel) {
        return ordersRepository.findByProductLabel(productLabel).orElseThrow(
                () -> new OrdersException(OrderErrorData.ORDER_NOT_FOUND)
        );
    }
}
