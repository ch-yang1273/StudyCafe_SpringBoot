package asc.portfolio.ascSb.scheduler;

import asc.portfolio.ascSb.common.domain.CurrentTimeProvider;
import asc.portfolio.ascSb.seat.service.SeatScheduleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class SeatStatusScheduler {

    private final SeatScheduleService seatScheduleService;
    private final CurrentTimeProvider currentTimeProvider;

    @Scheduled(fixedDelay = 1000 * 60)
    public void updateSeatAndTicketState() {
        seatScheduleService.terminateExpiredSeatsStatus(currentTimeProvider.localDateTimeNow());
        seatScheduleService.updateApproachingExpiredSeatsStatus(currentTimeProvider.localDateTimeNow());
    }
}
