package asc.portfolio.ascSb.seat.dto;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class SeatUsageEndingSoonEvent {

    private final Long userId;
    private final Long seatId;
    private final LocalDateTime timestamp;

    public SeatUsageEndingSoonEvent(Long userId, Long seatId, LocalDateTime timestamp) {
        this.userId = userId;
        this.seatId = seatId;
        this.timestamp = timestamp;
    }
}
