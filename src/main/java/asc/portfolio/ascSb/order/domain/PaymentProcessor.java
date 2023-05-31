package asc.portfolio.ascSb.order.domain;

import asc.portfolio.ascSb.bootpay.infra.BootPayApi;
import asc.portfolio.ascSb.cafe.domain.Cafe;
import asc.portfolio.ascSb.cafe.domain.CafeFinder;
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

    public void confirmPayment(Long userId, String receiptId) {
        Orders order = orderFinder.findByReceiptId(receiptId);

        int verificationPrice = order.getPrice();
        boolean isValid = bootPayApi.confirmPayment(receiptId, verificationPrice);

        if (isValid) {
            // 결제 승인
            TicketCreationInfo info = TicketCreationInfo.builder()
                    .userId(userId)
                    .cafeId(order.getCafeId())
                    .price(order.getPrice())
                    .typeString(order.getOrderType().getLabel())
                    .days(order.getOrderType().getDays())
                    .totalDuration(order.getOrderType().getMinute())
                    .productLabel(order.getProductLabel())
                    .build();

            ticketService.createTicket(info);
            order.completeOrder();
        } else {
            // 결제 승인 실패
            order.failedToConfirmOrder();
            // todo : Exception 던지고 싶은데 그러면 order 변경 내용이 업데이트 되지 않는다.
            //  -> 이벤트 방식으로 변경!
            // 결과를 이벤트에 넣어서 Raise하고, Listener에서 Complete, Failed를 결정하도록
            // 여기서는 이벤트만 만들고 던지고
            // throw new OrdersException(OrderErrorData.ORDER_CONFIRM_FAILED);
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

        ticketService.setInvalidTicket(orderId);
    }
}