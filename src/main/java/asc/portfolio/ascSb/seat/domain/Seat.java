package asc.portfolio.ascSb.seat.domain;
import asc.portfolio.ascSb.cafe.domain.Cafe;
import asc.portfolio.ascSb.ticket.domain.Ticket;
import asc.portfolio.ascSb.user.domain.User;
import lombok.*;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "SEAT")
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "R_ID", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "C_ID")
    private Cafe cafe; // CAFE FK

    @Column(name = "SN", nullable = false) // 1~40번
    private int seatNumber;

    // 좌석 상태
    @Column(name = "IS_RESERVED")
    private boolean isReserved;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", unique = true)
    private User user;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "T_ID")
    private Ticket ticket;

    @Builder
    public Seat(int seatNumber, Cafe cafe) {
        this.cafe = cafe;
        this.seatNumber = seatNumber;
        this.isReserved = false;
    }

    public void reserveSeat(User user, Ticket ticket) {
        this.user = user;
        this.ticket = ticket;
        this.isReserved = true;
    }

    public void exitSeat() {
        this.user = null;
        this.ticket = null;
        this.isReserved = false;
    }
}
