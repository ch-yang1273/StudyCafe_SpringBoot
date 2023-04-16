package asc.portfolio.ascSb.ticket.dto;

import asc.portfolio.ascSb.ticket.domain.Ticket;
import asc.portfolio.ascSb.ticket.domain.TicketStatus;
import asc.portfolio.ascSb.ticket.domain.TicketType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@Getter
public class TicketStatusResponse {

    private Long id;
    private TicketType type;
    private TicketStatus status;
    private LocalDate expiryTime;   // for Fixed-Ticket
    private Long totalDuration;     // for Part-time Ticket
    private Long remainTime;        // for Part-time Ticket

    @Builder
    public TicketStatusResponse(Long id, TicketType type, TicketStatus status,
                                LocalDate expiryTime, Long totalDuration, Long remainTime) {
        this.id = id;
        this.type = type;
        this.status = status;
        this.expiryTime = expiryTime;
        this.totalDuration = totalDuration;
        this.remainTime = remainTime;
    }

    public static TicketStatusResponse of(Ticket ticket) {
        return TicketStatusResponse.builder()
                .id(ticket.getId())
                .type(ticket.getTicketType())
                .status(ticket.getStatus())
                .expiryTime(ticket.getExpiryDate())
                .totalDuration(ticket.getTotalDuration())
                .remainTime(ticket.getRemainMinute())
                .build();
    }
}
