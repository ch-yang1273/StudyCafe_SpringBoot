package asc.portfolio.ascSb.order.domain;

import asc.portfolio.ascSb.order.exception.OrderErrorData;
import asc.portfolio.ascSb.order.exception.OrdersException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class OrderFinder {

    private final OrdersRepository ordersRepository;

    public Orders findById(Long orderId) {
        return ordersRepository.findById(orderId).orElseThrow(
                () -> new OrdersException(OrderErrorData.ORDER_NOT_FOUND)
        );
    }

    public List<Orders> findOrdersByUserId(Long userId) {
        return ordersRepository.findByUserId(userId);
    }

    public List<Orders> findOrdersByUserIdAndCafeId(Long userId, Long cafeId) {
        return ordersRepository.findByUserIdAndCafeId(userId, cafeId);
    }

    public Orders findByReceiptId(String receiptId) {
        return ordersRepository.findByReceiptId(receiptId).orElseThrow(
                () -> new OrdersException(OrderErrorData.ORDER_NOT_FOUND)
        );
    }
}
