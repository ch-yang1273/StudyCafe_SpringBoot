package asc.portfolio.ascSb.seat.domain;
import asc.portfolio.ascSb.seat.exception.SeatErrorData;
import asc.portfolio.ascSb.seat.exception.SeatException;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Slf4j
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

    @Enumerated(EnumType.STRING)
    @Column(name = "USAGE_STATUS", nullable = false)
    private SeatUsageStatus usageStatus;

    @Embedded
    private UsageData usageData;

    @Builder
    public Seat(Long id, Long cafeId, int seatNumber) {
        this.id = id;
        this.cafeId = cafeId;
        this.seatNumber = seatNumber;
        this.usageStatus = SeatUsageStatus.NOT_IN_USE;
    }

    private UsageData getUsageData() {
        return usageData;
    }

    public boolean canReserve() {
        return usageStatus.equals(SeatUsageStatus.NOT_IN_USE);
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

    public void startSeatUsage(UsageData usageData) {
        if (!canReserve()) {
            throw new SeatException(SeatErrorData.NOT_AVAILABLE_SEAT);
        }
        this.usageStatus = SeatUsageStatus.IN_USE;
        this.usageData = usageData;
    }

    public void scheduleSeatUsageTermination() {
        this.usageStatus = SeatUsageStatus.SCHEDULED_FOR_TERMINATION;
        this.usageData = null;
    }

    public void terminateSeatUsage() {
        this.usageStatus = SeatUsageStatus.NOT_IN_USE;
        this.usageData = null;
    }

    public void changeUsageStatusEndingSoon() {
        this.usageStatus = SeatUsageStatus.ENDING_SOON;
    }

    public boolean isBelongTo(Long cafeId) {
        return this.cafeId.equals(cafeId);
    }

    public void checkBelongToOrElseThrow(Long cafeId) {
        if (!isBelongTo(cafeId)) {
            throw new SeatException(SeatErrorData.NOT_MATCHED_SEAT_CAFE);
        }
    }
}
