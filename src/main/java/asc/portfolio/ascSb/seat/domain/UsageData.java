package asc.portfolio.ascSb.seat.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.time.Duration;
import java.time.LocalDateTime;

@Embeddable
public class UsageData {

    @Column(name = "USER_ID")
    private Long userId;

    @Column(name = "TICKET_ID")
    private Long ticketId;

    @Column(name = "START_TIME")
    private LocalDateTime startTime;

    protected UsageData() {
    }

    public UsageData(Long userId, Long ticketId, LocalDateTime startTime) {
        this.userId = userId;
        this.ticketId = ticketId;
        this.startTime = startTime;
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

    public Long getUsageDuration(LocalDateTime now) {
        return Duration.between(startTime, now).toMinutes();
    }
}
