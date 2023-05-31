package asc.portfolio.ascSb.order.service;

import asc.portfolio.ascSb.order.domain.OrderFinder;
import asc.portfolio.ascSb.order.domain.Orders;
import asc.portfolio.ascSb.order.dto.ConfirmPaymentEvent;
import asc.portfolio.ascSb.ticket.service.TicketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class OrderEventListener {

    private final OrderFinder orderFinder;
    private final TicketService ticketService;

    @EventListener
    public void handleConfirmPaymentEvent(ConfirmPaymentEvent event) {
        log.info("ConfirmPaymentEvent: OrderId={}, TimeStamp={}", event.getOrderId(), event.getTimestamp());

        Orders order = orderFinder.findById(event.getOrderId());
        if (event.isComplete()) {
            ticketService.createTicket(event.getInfo());
            order.completeOrder();
        } else {
            order.failedToConfirmOrder();
        }
    }
}
