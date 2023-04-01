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
        this.usageStatus = SeatUsageStatus.IN_USE;
        this.usageData = usageData;
    }

    public void endSeatUsage() {
        this.usageStatus = SeatUsageStatus.SCHEDULED_FOR_TERMINATION;
        this.usageData = null;
        log.debug("Seat.terminate");
        log.debug("seatNumber = {}", seatNumber);
        //todo : event 발생. PartTime이라면 ticket에 남은 시간 정리, Reservation 정리
    }

    public void changeUsageStatusEndingSoon() {
        this.usageStatus = SeatUsageStatus.ENDING_SOON;
        log.debug("Seat.changeUsageStatusEndingSoon");
        log.debug("seatNumber = {}", seatNumber);
        //todo 좌석 사용자들에게 종료 예정 FCM 알림
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
