package asc.portfolio.ascSb.order.dto;

import asc.portfolio.ascSb.ticket.dto.TicketCreationInfo;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ConfirmPaymentEvent {

    private final boolean isComplete;
    private final Long orderId;
    private final TicketCreationInfo info;

    private final LocalDateTime timestamp;

    public ConfirmPaymentEvent(boolean isComplete, Long orderId, TicketCreationInfo info, LocalDateTime timestamp) {
        this.isComplete = isComplete;
        this.orderId = orderId;
        this.info = info;
        this.timestamp = timestamp;
    }
}
