package asc.portfolio.ascSb.scheduler;

import asc.portfolio.ascSb.seat.service.SeatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Profile("!test") // test 프로필이 아닌 경우에만 활성화
@RequiredArgsConstructor
@Component
public class TableUpdateScheduler {

    private final SeatService seatService;

    @Scheduled(fixedDelay = 1000 * 60)
    public void updateSeatAndTicketState() {
        seatService.terminateExpiredSeatsStatus();
        seatService.updateApproachingExpiredSeatsStatus();
    }
}
