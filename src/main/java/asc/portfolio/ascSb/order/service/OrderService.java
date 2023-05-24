package asc.portfolio.ascSb.order.service;

import asc.portfolio.ascSb.bootpay.dto.BootPayReceipt;
import asc.portfolio.ascSb.bootpay.infra.BootPayApi;
import asc.portfolio.ascSb.cafe.domain.Cafe;
import asc.portfolio.ascSb.cafe.domain.CafeFinder;
import asc.portfolio.ascSb.order.domain.OrderFinder;
import asc.portfolio.ascSb.order.domain.OrderStatus;
import asc.portfolio.ascSb.order.domain.Orders;
import asc.portfolio.ascSb.order.domain.OrdersRepository;
import asc.portfolio.ascSb.order.dto.OrderRequest;
import asc.portfolio.ascSb.order.dto.OrderResponse;
import asc.portfolio.ascSb.order.exception.OrderErrorData;
import asc.portfolio.ascSb.order.exception.OrdersException;
import asc.portfolio.ascSb.ticket.service.TicketService;
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
    private final OrderFinder orderFinder;
    private final UserFinder userFinder;
    private final CafeFinder cafeFinder;

    private final BootPayApi bootPayApi;

    //todo 삭제
    private final TicketService ticketService;

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
        }

        return orderFinder.findOrdersByUserId(targetUser.getId())
                .stream()
                .map(OrderResponse::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public void confirmPayment(Long userId, String receiptId) {
        Orders order = orderFinder.findByReceiptId(receiptId);

        // Validation
        int price = order.getPrice();
        BootPayReceipt dto = bootPayApi.getReceipt(receiptId);
        boolean isValid = bootPayApi.crossValidation(dto, 1, price);

        // 결제 승인
        if (isValid) {
            BootPayReceipt confirm = bootPayApi.confirm(receiptId);
            log.info("Confirm receiptId={}, at={}", receiptId, confirm.getPurchased_at());
            order.completeOrder();

            /* 검증 완료시 orders 상태 Done(완료)으로 변경 Ticket에 이용권추가 */
            ticketService.saveProductToTicket(userId, order);
            return;
        }

        // 결제 승인 실패
        order.failedToConfirmOrder();
        throw new OrdersException(OrderErrorData.ORDER_CONFIRM_FAILED);
    }

    public void cancelPayment(Long adminId, Long orderId) {
        Cafe adminCafe = cafeFinder.findByAdminId(adminId);
        Orders order = orderFinder.findById(orderId);

        // 결제 취소 권한 확인 (관리자 취소)
        if (!order.getCafeId().equals(adminCafe.getId())) {
            throw new OrdersException(OrderErrorData.ORDER_CANCEL_NO_AUTH);
        }

        // 결제 취소
        String receiptId = order.getReceiptId();
        BootPayReceipt receipt = bootPayApi.getReceipt(receiptId);
        BootPayReceipt cancelReceipt = bootPayApi.cancelReceipt(receiptId);

        ticketService.setInvalidTicket(orderId);
    }
}
