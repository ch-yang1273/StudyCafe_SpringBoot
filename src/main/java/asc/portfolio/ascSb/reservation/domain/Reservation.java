package asc.portfolio.ascSb.reservation.domain;

import asc.portfolio.ascSb.common.domain.BaseTimeEntity;
import asc.portfolio.ascSb.cafe.domain.Cafe;
import asc.portfolio.ascSb.seat.domain.Seat;
import asc.portfolio.ascSb.ticket.domain.Ticket;
import asc.portfolio.ascSb.user.domain.User;
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
    private ReservationStatus status;

    @Builder
    public Reservation(User user, Cafe cafe, Seat seat, Ticket ticket, LocalDateTime startTime) {
        this.userId = user.getId();
        this.cafeId = cafe.getId();
        this.seatId = seat.getId();
        this.ticketId = ticket.getId();
        this.startTime = startTime;

        this.status = ReservationStatus.IN_USE;
    }

    public void endUsingSeat(LocalDateTime endTime) {
        this.status = ReservationStatus.END_OF_USE;
        this.endTime = endTime;
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
