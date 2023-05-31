package asc.portfolio.ascSb.order.domain;

import asc.portfolio.ascSb.bootpay.infra.BootPayApi;
import asc.portfolio.ascSb.cafe.domain.Cafe;
import asc.portfolio.ascSb.cafe.domain.CafeFinder;
import asc.portfolio.ascSb.common.domain.CommonEventsPublisher;
import asc.portfolio.ascSb.common.domain.CurrentTimeProvider;
import asc.portfolio.ascSb.order.dto.ConfirmPaymentEvent;
import asc.portfolio.ascSb.order.exception.OrderErrorData;
import asc.portfolio.ascSb.order.exception.OrdersException;
import asc.portfolio.ascSb.ticket.dto.TicketCreationInfo;
import asc.portfolio.ascSb.ticket.service.TicketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class PaymentProcessor {

    private final OrderFinder orderFinder;
    private final CafeFinder cafeFinder;
    private final BootPayApi bootPayApi;
    private final TicketService ticketService;

    private final CommonEventsPublisher eventsPublisher;
    private final CurrentTimeProvider currentTimeProvider;

    public void confirmPayment(Long userId, String receiptId) {
        Orders order = orderFinder.findByReceiptId(receiptId);

        int verificationPrice = order.getPrice();
        boolean isValid = bootPayApi.confirmPayment(receiptId, verificationPrice);

        TicketCreationInfo info = TicketCreationInfo.builder()
                .userId(userId)
                .cafeId(order.getCafeId())
                .price(order.getPrice())
                .typeString(order.getOrderType().getLabel())
                .days(order.getOrderType().getDays())
                .totalDuration(order.getOrderType().getMinute())
                .productLabel(order.getProductLabel())
                .build();

        // todo : Exception이 있어도 Event는 제대로 Publish 되는지 확인! 비동기면 된다.
        // 이벤트 발행 : OrdersException이 있어도 order가 업데이트 되어야 함.
        ConfirmPaymentEvent event = new ConfirmPaymentEvent(isValid, order.getId(), info,
                currentTimeProvider.localDateTimeNow());
        eventsPublisher.raise(event);

        if (!isValid) {
            throw new OrdersException(OrderErrorData.ORDER_CONFIRM_FAILED);
        }
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
        bootPayApi.cancelPayment(receiptId);

        ticketService.cancelTicket(order.getProductLabel());
        order.cancel();
    }
}
