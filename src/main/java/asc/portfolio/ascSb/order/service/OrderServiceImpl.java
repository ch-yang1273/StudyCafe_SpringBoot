package asc.portfolio.ascSb.order.service;


import asc.portfolio.ascSb.order.domain.Orders;
import asc.portfolio.ascSb.order.domain.OrdersRepository;
import asc.portfolio.ascSb.order.domain.OrderStateType;
import asc.portfolio.ascSb.user.domain.User;
import asc.portfolio.ascSb.order.dto.OrderDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrdersRepository ordersRepository;

    @Override
    public Long saveOrder(User user, OrderDto orderDto) {
        orderDto.setUserId(user.getLoginId());
        orderDto.setOrderStateType(OrderStateType.ORDER);
        Orders orders = orderDto.toEntity();
        Orders saveOrders = ordersRepository.save(orders);
        return saveOrders.getId();
    }

    @Override
    public Orders findReceiptOrderId(String receiptId) {
        return ordersRepository.findByReceiptOrderIdContains(receiptId);
    }

    @Override
    public Orders findReceiptIdToProductLabel(String productLabel) {
        return ordersRepository.findByProductLabelContains(productLabel);
    }
}
