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
    @Column(name = "RESERVATION_ID", nullable = false)
    private Long id; // 사용구분을 위한 table 입니다 ex) 몇시,몇분에 어느좌석에 어떤 user가 몇시간을 사용했다~

    @Enumerated(EnumType.STRING)
    private ReservationStatus isValid;

    //Entity User
    @Column(name = "USER_ID")
    private String userLoginId;
    //Entity Cafe
    @Column(name = "C_NAME")
    private String cafeName;
    //Entity Cafe + Seat
    @Column(name = "S_N")
    private Integer seatNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "T_ID") // 어떤 ticket을
    private Ticket ticket;

    @Column(name = "S_T")
    private Long startTime;

    @Column(name = "END_T")
    private LocalDateTime endTime; // 좌석 종료 예정 시간. (now + startTime)

    private Long timeInUse; // 실제 사용한 시간, 분단위

    @Builder
    public Reservation(User user, Cafe cafe, Seat seat, Ticket ticket, Long startTime, Long timeInUse) {
        this.userLoginId = user.getLoginId();
        this.cafeName = cafe.getCafeName();
        this.seatNumber = seat.getSeatNumber();
        this.ticket = ticket;
        this.startTime = startTime;
        this.timeInUse = timeInUse;
        //자동 값 입력
        this.endTime = LocalDateTime.now().plusMinutes(startTime);
        this.isValid = ReservationStatus.VALID;
    }

    private void getTimeInUse() {
    }

    public Long updateTimeInUse() {
        this.timeInUse = Duration.between(getCreateDate(), LocalDateTime.now()).toMinutes();
        return timeInUse;
    }

    public Long endUsingSeat() {
        this.isValid = ReservationStatus.INVALID;
        this.timeInUse = Duration.between(getCreateDate(), LocalDateTime.now()).toMinutes();
        return timeInUse;
    }
}
