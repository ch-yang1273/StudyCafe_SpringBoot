package asc.portfolio.ascSb.ticket.dto;


import asc.portfolio.ascSb.ticket.domain.Ticket;
import asc.portfolio.ascSb.ticket.domain.TicketStatus;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class TicketForAdminResponse {

    private TicketStatus isValidTicket;
    private LocalDate expiryDate; // 기간제 티켓 날짜 => fixedTermTicket - createDate 시간으로 남은기간 계산
    private Long partTimeTicket; // 결제한 시간제 티켓시간 // 50시간, 100시간
    private Long remainingTime; // 시간제 티켓 남은시간
    private String productLabel;
    private Integer ticketPrice;

    private LocalDateTime createDate;
    private LocalDateTime modifiedDate;

    private String productNameType;

    private long period;

    public TicketForAdminResponse(Ticket ticket, String productLabel) {
        this.isValidTicket = ticket.getStatus();
        this.expiryDate = ticket.getExpiryDate();
        this.partTimeTicket = ticket.getTotalDuration();
        this.remainingTime = ticket.getRemainMinute();
        this.productLabel = ticket.getProductLabel();
        this.ticketPrice = ticket.getPrice();
        this.createDate = ticket.getCreateDate();
        this.modifiedDate = ticket.getModifiedDate();
        this.productNameType = productLabel;
    }
}
