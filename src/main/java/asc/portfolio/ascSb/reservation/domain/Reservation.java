package asc.portfolio.ascSb.reservation.domain;

import asc.portfolio.ascSb.common.domain.BaseTimeEntity;
import asc.portfolio.ascSb.seat.domain.UsageData;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.Duration;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "RESERVATION")
public class Reservation extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RESERVATION_ID")
    private Long id;

    @Column(name = "USER_ID")
    private Long userId;

    @Column(name = "CAFE_ID")
    private Long cafeId;

    @Column(name = "SEAT_ID")
    private Long seatId;

    @Column(name = "TICKET_ID")
    private Long ticketId;

    @Column(name = "START_TIME")
    private LocalDateTime startTime;

    @Column(name = "END_TIME")
    private LocalDateTime endTime; // 좌석 종료 된 시간

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS")
    private ReservationStatus status;

    @Builder
    public Reservation(Long userId, Long cafeId, Long seatId, Long ticketId, LocalDateTime startTime) {
        this.userId = userId;
        this.cafeId = cafeId;
        this.seatId = seatId;
        this.ticketId = ticketId;

        this.startTime = startTime;
        this.status = ReservationStatus.IN_USE;
    }

    public UsageData toUsageData() {
        return new UsageData(this.userId, this.ticketId, this.startTime);
    }

    public void release(LocalDateTime now) {
        this.status = ReservationStatus.END_OF_USE;
        this.endTime = now;
    }

    public Long getUsageMinute(LocalDateTime now) {
        long minute = Duration.between(this.startTime, now).toMinutes();
        if (minute < 0) {
            //todo : Exception 수정
            throw new RuntimeException("음수 시간");
        }
        return minute;
    }
}
