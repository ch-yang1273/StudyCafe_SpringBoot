package asc.portfolio.ascSb.ticket.domain;
import asc.portfolio.ascSb.common.domain.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;

@Slf4j
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "TICKET")
public class Ticket extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TICKET_ID", nullable = false)
    private Long id;

    @Column(name ="USER_ID")
    private Long userId;

    @Column(name = "CAFE_ID")
    private Long cafeId;

    @Enumerated(EnumType.STRING)
    @Column(name = "TYPE")
    private TicketType ticketType;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS")
    private TicketStatus status;

    @Column(name = "PRICE")
    private Integer price;

    @Column(name = "EXPIRY_DATE")
    private LocalDate expiryDate;

    @Column(name = "TOTAL_DURATION")
    private Long totalDuration;

    @Column(name = "REMAIN_MINUTE") // 시간제 티켓 남은시간 (분단위)
    private Long remainMinute;

    //todo : ticket Id로 사용합시다.
    @Column(unique = true)
    private String productLabel;

    @Builder
    public Ticket(Long id, Long userId, Long cafeId, TicketType ticketType, TicketStatus status, Integer price,
                  LocalDate expiryDate, Long totalDuration, Long remainMinute, String productLabel) {
        this.id = id;
        this.userId = userId;
        this.cafeId = cafeId;
        this.ticketType = ticketType;
        this.status = status;
        this.price = price;
        this.expiryDate = expiryDate;
        this.totalDuration = totalDuration;
        this.remainMinute = remainMinute;
        this.productLabel = productLabel;
    }

    public Ticket(Long cafeId, Long userId, TicketStatus status, Integer price, LocalDate expiryDate,
                  Long totalDuration, Long remainMinute, String productLabel) {
        this.cafeId = cafeId;
        this.userId = userId;
        this.status = status;
        this.price = price;
        this.expiryDate = expiryDate;
        this.totalDuration = totalDuration;
        this.remainMinute = remainMinute;
        this.productLabel = productLabel;
    }

    public void changeTicketStateToInvalid() {
        status = TicketStatus.END_OF_USE;
    }

    public void exitUsingTicket(Long useTime) {
        if (this.isExpiryDate()) {
            this.isValidFixedTermTicket();
        } else {
            // partTime Ticket 일 때만 time 파라미터 사용
            remainMinute -= useTime;
            if (remainMinute <= 0) {
                this.changeTicketStateToInvalid();
            }
            if (remainMinute < 0) {
                log.error("Ticket.remainingTime is under 0");
            }
        }
    }

    /**
     * @return true : FixedTerm Ticket, false : PartTime Ticket
     */
    public boolean isExpiryDate() {
        if ((expiryDate != null) && (totalDuration == null) && (remainMinute == null)) {
            return true;
        } else if ((totalDuration != null) && (remainMinute != null)) {
            return false;
        } else {
            log.error("알 수 없는 Ticket 상태입니다.");
            throw new IllegalStateException("알 수 없는 Ticket 상태입니다.");
        }
    }

    public boolean isOfType(TicketType type) {
        return this.getTicketType().equals(type);
    }

    public boolean isNotOfType(TicketType type) {
        return !isOfType(type);
    }

    public boolean isValidFixedTermTicket() {
        if (expiryDate != null) {
            boolean isValid = LocalDate.now().isBefore(expiryDate);
            if (!isValid) {
                this.changeTicketStateToInvalid();
            }
            return isValid;
        } else {
            return false;
        }
    }

    public boolean isValidPartTimeTicket() {
        if ((totalDuration != null) && (remainMinute != null)) {
            if (remainMinute > 0) {
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

    public void extendRemainingMinute(long minuteToExtend) {
        this.remainMinute += minuteToExtend;
    }

    public void extendExpiryDate(long daysToExtend) {
        this.expiryDate = this.expiryDate.plusDays(daysToExtend);
    }
}
