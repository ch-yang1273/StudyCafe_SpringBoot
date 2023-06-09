package asc.portfolio.ascSb.order.service;

import asc.portfolio.ascSb.cafe.domain.Cafe;
import asc.portfolio.ascSb.cafe.domain.CafeFinder;
import asc.portfolio.ascSb.order.domain.OrderFinder;
import asc.portfolio.ascSb.order.domain.OrderStatus;
import asc.portfolio.ascSb.order.domain.Orders;
import asc.portfolio.ascSb.order.domain.OrdersRepository;
import asc.portfolio.ascSb.order.domain.PaymentProcessor;
import asc.portfolio.ascSb.order.dto.OrderRequest;
import asc.portfolio.ascSb.order.dto.OrderResponse;
import asc.portfolio.ascSb.user.domain.User;
import asc.portfolio.ascSb.user.domain.UserFinder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class OrderService {

    private final OrdersRepository ordersRepository;
    private final PaymentProcessor paymentProcessor;

    private final OrderFinder orderFinder;
    private final UserFinder userFinder;
    private final CafeFinder cafeFinder;

    @Transactional
    public void saveOrder(Long userId, OrderRequest dto) {
        Orders orders = Orders.builder()
                .status(OrderStatus.PAYMENT_PROCESSING)
                .userId(userId)
                .productLabel(dto.getProductLabel())
                .orderType(dto.getOrderType())
                .price(dto.getPrice())
                .receiptId(dto.getReceiptId())
                .build();

        Orders saveOrders = ordersRepository.save(orders);
        log.info("주문번호={}", saveOrders.getId());
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> findOrders(Long authUserId, String loginId) {
        User targetUser = userFinder.findByLoginId(loginId);

        if (!authUserId.equals(targetUser.getId())) {
            // Cafe admin 검색. 자신의 Cafe에 관한 정보만 반환
            Cafe adminCafe = cafeFinder.findByAdminId(authUserId);
            return orderFinder.findOrdersByUserIdAndCafeId(targetUser.getId(), adminCafe.getId())
                    .stream()
                    .map(OrderResponse::new)
                    .collect(Collectors.toList());
        } else {
            // User 본인 검색, 자신의 모든 정보를 반환
            return orderFinder.findOrdersByUserId(targetUser.getId())
                    .stream()
                    .map(OrderResponse::new)
                    .collect(Collectors.toList());
        }
    }

    @Transactional
    public void confirmPayment(Long userId, String receiptId) {
        paymentProcessor.confirmPayment(userId, receiptId);
    }

    public void cancelPayment(Long adminId, Long orderId) {
        paymentProcessor.cancelPayment(adminId, orderId);
    }
}
