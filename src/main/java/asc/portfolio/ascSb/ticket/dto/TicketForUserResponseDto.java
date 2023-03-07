package asc.portfolio.ascSb.ticket.dto;

import asc.portfolio.ascSb.ticket.domain.Ticket;
import asc.portfolio.ascSb.ticket.domain.TicketStateType;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TicketForUserResponseDto {

    private TicketStateType isValidTicket;
    private LocalDateTime fixedTermTicket; // 기간제 티켓 날짜 => fixedTermTicket - createDate 시간으로 남은기간 계산
    private Long partTimeTicket; // 결제한 시간제 티켓시간 // 50시간, 100시간
    private Long remainingTime; // 시간제 티켓 남은시간

    private String productLabel;

    private long period;

    public void setTicketToTicketResponseDto(Ticket ticket) {
        isValidTicket = ticket.getIsValidTicket();
        fixedTermTicket = ticket.getFixedTermTicket();
        partTimeTicket = ticket.getPartTimeTicket();
        remainingTime = ticket.getRemainingTime();
        productLabel = ticket.getProductLabel();
    }

    public Ticket toEntity() {
        return Ticket.builder()
                .isValidTicket(isValidTicket)
                .fixedTermTicket(fixedTermTicket)
                .partTimeTicket(partTimeTicket)
                .remainingTime(remainingTime)
                .productLabel(productLabel)
                .build();
    }
}
