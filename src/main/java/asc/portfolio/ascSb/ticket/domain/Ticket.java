package asc.portfolio.ascSb.ticket.domain;
import asc.portfolio.ascSb.common.domain.BaseTimeEntity;
import asc.portfolio.ascSb.ticket.exception.TicketErrorData;
import asc.portfolio.ascSb.ticket.exception.TicketException;
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

    @Column(name = "ORDER_ID", unique = true)
    private Long orderId;

    @Enumerated(EnumType.STRING)
    @Column(name = "TYPE")
    private TicketType ticketType;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS")
    private TicketStatus status;

    @Column(name = "PRICE")
    private Integer price;

    // Fixed-Term Ticket
    @Column(name = "EXPIRY_DATE")
    private LocalDate expiryDate;

    // Part-Time Ticket
    @Column(name = "TOTAL_DURATION")
    private Long totalDuration;
    @Column(name = "REMAIN_MINUTE")
    private Long remainMinute;

    @Column(unique = true)
    private String productLabel;

    @Builder
    public Ticket(Long id, Long userId, Long cafeId, Long orderId, TicketType ticketType, TicketStatus status,
                  Integer price, LocalDate expiryDate, Long totalDuration, Long remainMinute, String productLabel) {
        this.id = id;
        this.userId = userId;
        this.cafeId = cafeId;
        this.orderId = orderId;
        this.ticketType = ticketType;
        this.status = status;
        this.price = price;
        this.expiryDate = expiryDate;
        this.totalDuration = totalDuration;
        this.remainMinute = remainMinute;
        this.productLabel = productLabel;
    }

    public void cancel() {
        status = TicketStatus.CANCEL;
    }

    public boolean isTicketUsable() {
        return this.status == TicketStatus.IN_USE;
    }

    public void checkTicketUsable() {
        if (!isTicketUsable()) {
            throw new TicketException(TicketErrorData.TICKET_NOT_USABLE);
        }
    }

    public boolean isOfType(TicketType type) {
        return this.getTicketType().equals(type);
    }

    public boolean isNotOfType(TicketType type) {
        return !isOfType(type);
    }

    public void extendRemainingMinute(long minuteToExtend) {
        if (isNotOfType(TicketType.PART_TERM)) {
            throw new TicketException(TicketErrorData.NOT_PART_TIME_TICKET);
        }
        this.remainMinute += minuteToExtend;
    }

    public void decreaseRemainingMinutes(long minuteToDecrease) {
        this.remainMinute -= minuteToDecrease;
        if (this.remainMinute <= 0) {
            status = TicketStatus.END_OF_USE;
        }
    }

    public void extendExpiryDate(long daysToExtend) {
        if (isNotOfType(TicketType.FIXED_TERM)) {
            throw new TicketException(TicketErrorData.NOT_FIXED_TERM_TICKET);
        }
        this.expiryDate = this.expiryDate.plusDays(daysToExtend);
    }
}
