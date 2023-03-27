package asc.portfolio.ascSb.seat.dto;

import asc.portfolio.ascSb.cafe.domain.Cafe;
import asc.portfolio.ascSb.seat.domain.Seat;
import asc.portfolio.ascSb.seat.domain.UsageData;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class SeatStatusResponse {

    private String cafeName;
    private Integer seatNumber;
    private Long cafeId;
    private Long seatId;

    private boolean isReserved;
    private Long userId;
    private Long ticketId;
    private LocalDateTime startTime;
    private Long duration;

    public SeatStatusResponse(Cafe cafe, Seat seat, LocalDateTime now) {
        this.cafeName = cafe.getCafeName();
        this.seatNumber = seat.getSeatNumber();
        this.cafeId = cafe.getId();
        this.seatId = seat.getId();

        this.isReserved = seat.isReserved();
        UsageData usageData = seat.getUsageData();
        this.userId = usageData.getUserId();
        this.ticketId = usageData.getTicketId();
        this.startTime = usageData.getStartTime();
        this.duration = usageData.getUsageDuration(now);
    }
}
