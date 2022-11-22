package asc.portfolio.ascSb.web.dto.ticket;

import asc.portfolio.ascSb.domain.ticket.TicketStateType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Duration;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TicketSelectResponseDto {

    private TicketStateType isValidTicket;
    private LocalDateTime fixedTermTicket; // 기간제 티켓 날짜 => fixedTermTicket - createDate 시간으로 남은기간 계산
    private Integer partTimeTicket; // 결제한 시간제 티켓시간 // 50시간, 100시간
    private Integer remainingTime; // 시간제 티켓 남은시간

    private long period;

    public TicketSelectResponseDto(TicketSelectResponseDto entity) {
        this.isValidTicket = entity.getIsValidTicket();
        this.fixedTermTicket = entity.getFixedTermTicket();
        this.partTimeTicket = entity.getPartTimeTicket();
        this.remainingTime = entity.getRemainingTime();
    }
}
