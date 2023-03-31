package asc.portfolio.ascSb.seat.dto;

import asc.portfolio.ascSb.cafe.domain.Cafe;
import asc.portfolio.ascSb.seat.domain.Seat;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
public class SeatStatusResponse {

    private String cafeName;
    private Integer seatNumber;
    private Long cafeId;
    private Long seatId;

    private boolean isReserved;
    private Long userId;
    private Long ticketId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Long duration;

    public SeatStatusResponse(Cafe cafe, Seat seat, LocalDateTime now) {
        this.cafeName = cafe.getCafeName();
        this.seatNumber = seat.getSeatNumber();
        this.cafeId = cafe.getId();
        this.seatId = seat.getId();

        this.isReserved = seat.isReserved();
        this.userId = seat.getUserId();
        this.ticketId = seat.getTicketId();
        this.startTime = seat.getStartTime();
        this.endTime = seat.getEndTime();
        this.duration = seat.getUsageDuration(now);
    }
}
