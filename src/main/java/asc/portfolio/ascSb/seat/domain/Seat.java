package asc.portfolio.ascSb.seat.domain;
import asc.portfolio.ascSb.ticket.domain.Ticket;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

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

    // 좌석 예약자
    @Column(name = "USER_ID", unique = true)
    private Long userId;

    // todo : 추후 Id 참조로 변경
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "T_ID")
    private Ticket ticket;

    @Builder
    public Seat(Long cafeId, int seatNumber) {
        this.cafeId = cafeId;
        this.seatNumber = seatNumber;
        this.isReserved = false;
    }

    public void reserveSeat(Long userId, Ticket ticket) {
        this.userId = userId;
        this.ticket = ticket;
        this.isReserved = true;
    }

    public void exitSeat() {
        this.userId = null;
        this.ticket = null;
        this.isReserved = false;
    }
}
