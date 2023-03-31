package asc.portfolio.ascSb.seat.domain;
import asc.portfolio.ascSb.reservation.domain.Reservation;
import asc.portfolio.ascSb.seat.exception.SeatErrorData;
import asc.portfolio.ascSb.seat.exception.SeatException;
import asc.portfolio.ascSb.ticket.domain.Ticket;
import asc.portfolio.ascSb.ticket.domain.TicketType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "SEAT")
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SEAT_ID")
    private Long id;

    @Column(name = "CAFE_ID", nullable = false)
    private Long cafeId;

    @Column(name = "SEAT_NUM", nullable = false)
    private int seatNumber;

    // 좌석 예약 상태
    @Column(name = "IS_RESERVED")
    private boolean isReserved;

    @Embedded
    private UsageData usageData;

    @Builder
    public Seat(Long id, Long cafeId, int seatNumber) {
        this.id = id;
        this.cafeId = cafeId;
        this.seatNumber = seatNumber;
        this.isReserved = false;
    }

    private UsageData getUsageData() {
        return usageData;
    }

    public Long getUserId() {
        return usageData.getUserId();
    }

    public Long getTicketId() {
        return usageData.getTicketId();
    }

    public LocalDateTime getStartTime() {
        return usageData.getStartTime();
    }

    public LocalDateTime getEndTime() {
        return usageData.getEndTime();
    }

    public Long getUsageDuration(LocalDateTime now) {
        return usageData.getUsageDuration(now);
    }

    public void reserveSeat(Reservation reservation, Ticket ticket, LocalDateTime now) {
        ticket.isTicketUsable();

        LocalDateTime endTime = LocalDateTime.MAX;
        if (ticket.isOfType(TicketType.PART_TERM)) {
            endTime = now.plusMinutes(ticket.getRemainMinute());
        }

        this.isReserved = true;
        this.usageData = reservation.toUsageData(endTime);
    }

    public void exit() {
        this.isReserved = false;
        this.usageData = null;
    }

    public boolean isBelongTo(Long cafeId) {
        return this.cafeId.equals(cafeId);
    }

    public void isBelongToOrElseThrow(Long cafeId) {
        throw new SeatException(SeatErrorData.NOT_MATCHED_SEAT_CAFE);
    }
}
