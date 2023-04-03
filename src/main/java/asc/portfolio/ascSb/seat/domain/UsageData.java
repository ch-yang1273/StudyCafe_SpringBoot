package asc.portfolio.ascSb.seat.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.time.Duration;
import java.time.LocalDateTime;

@Embeddable
public class UsageData {

    @Column(name = "USER_ID", unique = true)
    private Long userId;

    @Column(name = "TICKET_ID", unique = true)
    private Long ticketId;

    @Column(name = "START_TIME")
    private LocalDateTime startTime;

    @Column(name = "END_TIME")
    private LocalDateTime endTime;

    protected UsageData() {
    }

    public UsageData(Long userId, Long ticketId, LocalDateTime startTime, LocalDateTime endTime) {
        this.userId = userId;
        this.ticketId = ticketId;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getTicketId() {
        return ticketId;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public Long getUsageDuration(LocalDateTime now) {
        return Duration.between(startTime, now).toMinutes();
    }
}
