package asc.portfolio.ascSb.order.domain;

import asc.portfolio.ascSb.bootpay.infra.BootPayApi;
import asc.portfolio.ascSb.ticket.service.TicketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class PaymentProcessor {

    private final OrderFinder orderFinder;
    private final BootPayApi bootPayApi;

    // todo 삭제
    private final TicketService ticketService;

    public void confirmPayment(Long userId, String receiptId) {
        Orders order = orderFinder.findByReceiptId(receiptId);

        int verificationPrice = order.getPrice();
        boolean isValid = bootPayApi.confirmPayment(receiptId, verificationPrice);

        if (isValid) {
            // 결제 승인
            /* 검증 완료시 orders 상태 Done(완료)으로 변경 Ticket에 이용권추가 */
            // todo : saveProductToTicket 정리, 도메인 서비스 호출로 변경
            ticketService.saveProductToTicket(userId, order);
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
}
