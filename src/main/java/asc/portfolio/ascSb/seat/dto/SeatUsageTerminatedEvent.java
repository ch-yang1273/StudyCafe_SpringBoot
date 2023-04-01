package asc.portfolio.ascSb.seat.dto;

import java.time.LocalDateTime;

public class SeatUsageTerminatedEvent {

    private final Long seatId;
    private final LocalDateTime timestamp;

    public SeatUsageTerminatedEvent(Long seatId, LocalDateTime timestamp) {
        this.seatId = seatId;
        this.timestamp = timestamp;
    }

    public Long getSeatId() {
        return seatId;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
