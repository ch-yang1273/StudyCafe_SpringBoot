package asc.portfolio.ascSb.ticket.domain;
import asc.portfolio.ascSb.common.domain.BaseTimeEntity;
import asc.portfolio.ascSb.cafe.domain.Cafe;
import asc.portfolio.ascSb.user.domain.User;
import asc.portfolio.ascSb.ticket.dto.TicketForUserResponseDto;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Getter
@ToString(exclude = {"cafe", "user"})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "TICKET")
public class Ticket extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "T_ID", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "C_ID")
    private Cafe cafe;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="USER_ID")
    private User user;

    @Enumerated(EnumType.STRING)
    @Basic(optional = false)
    @Column(columnDefinition = "enum('VALID','INVALID')")
    private TicketStateType isValidTicket;

    @Column(name = "T_P")
    private Integer ticketPrice;

    @Column(name = "FT_T")
    private LocalDateTime fixedTermTicket; // 기간제 티켓 날짜 => fixedTermTicket - createDate 시간으로 남은기간 계산

    @Column(name = "PT_T") // 결제한 시간제 티켓시간 (분단위)
    private Long partTimeTicket; // 50시간, 100시간

    @Column(name = "R_T") // 시간제 티켓 남은시간 (분단위)
    private Long remainingTime;

    @Column(unique = true)
    private String productLabel;

    @Builder
    public Ticket(Cafe cafe, User user, TicketStateType isValidTicket, Integer ticketPrice, LocalDateTime fixedTermTicket,
                  Long partTimeTicket, Long remainingTime, String productLabel) {
        this.cafe = cafe;
        this.user = user;
        this.isValidTicket = isValidTicket;
        this.ticketPrice = ticketPrice;
        this.fixedTermTicket = fixedTermTicket;
        this.partTimeTicket = partTimeTicket;
        this.remainingTime = remainingTime;
        this.productLabel = productLabel;
    }

    public void changeTicketStateToInvalid() {
        isValidTicket = TicketStateType.INVALID;
    }

    public void exitUsingTicket(Long useTime) {
        if (this.isFixedTermTicket()) {
            this.isValidFixedTermTicket();
        } else {
            // partTime Ticket 일 때만 time 파라미터 사용
            remainingTime -= useTime;
            if (remainingTime <= 0) {
                this.changeTicketStateToInvalid();
            }
            if (remainingTime < 0) {
                log.error("Ticket.remainingTime is under 0");
            }
        }
    }

    /**
     * @return true : FixedTerm Ticket, false : PartTime Ticket
     */
    public boolean isFixedTermTicket() {
        if ((fixedTermTicket != null) && (partTimeTicket == null) && (remainingTime == null)) {
            return true;
        } else if ((partTimeTicket != null) && (remainingTime != null)) {
            return false;
        } else {
            log.error("알 수 없는 Ticket 상태입니다.");
            throw new IllegalStateException("알 수 없는 Ticket 상태입니다.");
        }
    }

    public boolean isValidFixedTermTicket() {
        if (fixedTermTicket != null) {
            boolean isValid = LocalDateTime.now().isBefore(fixedTermTicket);
            if (!isValid) {
                this.changeTicketStateToInvalid();
            }
            return isValid;
        } else {
            return false;
        }
    }

    public boolean isValidPartTimeTicket() {
        if ((partTimeTicket != null) && (remainingTime != null)) {
            if (remainingTime > 0) {
                return true;
            } else {
                this.changeTicketStateToInvalid();
                return false;
            }
        } else {
            return false;
        }
    }

    public boolean isValidTicket() {
        if (this.isValidFixedTermTicket()) {
            return true;
        } else {
            return this.isValidPartTimeTicket();
        }
    }

    //TO DTO
    public Optional<TicketForUserResponseDto> toTicketResponseDto() {
        TicketForUserResponseDto dto = new TicketForUserResponseDto();
        dto.setTicketToTicketResponseDto(this);

        return Optional.of(dto);
    }

    public void updateTicketFixedTermTicket(LocalDateTime fixedTermTicket) { this.fixedTermTicket = fixedTermTicket; }

    public void updateTicketRemainingTime(Long remainingTime) { this.remainingTime = remainingTime; }
}
