package asc.portfolio.ascSb.scheduler;

import asc.portfolio.ascSb.seat.service.SeatScheduleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class TableUpdateScheduler {

    private final SeatScheduleService seatScheduleService;

    @Scheduled(fixedDelay = 1000 * 60)
    public void updateSeatAndTicketState() {
        seatScheduleService.terminateExpiredSeatsStatus();
        seatScheduleService.updateApproachingExpiredSeatsStatus();
    }
}
