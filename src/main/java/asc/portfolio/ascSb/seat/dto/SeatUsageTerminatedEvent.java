package asc.portfolio.ascSb.seat.dto;

import java.time.LocalDateTime;

public class SeatUsageTerminatedEvent {

    private final Long userId;
    private final Long seatId;
    private final LocalDateTime timestamp;

    public SeatUsageTerminatedEvent(Long userId, Long seatId, LocalDateTime timestamp) {
        this.userId = userId;
        this.seatId = seatId;
        this.timestamp = timestamp;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getSeatId() {
        return seatId;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
