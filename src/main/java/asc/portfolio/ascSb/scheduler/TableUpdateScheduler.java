package asc.portfolio.ascSb.scheduler;

import asc.portfolio.ascSb.seat.service.SeatService;
import asc.portfolio.ascSb.ticket.service.TicketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@Profile("!test") // test 프로필이 아닌 경우에만 활성화
@RequiredArgsConstructor
public class TableUpdateScheduler {

    private final SeatService seatService;
    private final TicketService ticketService;

    @Scheduled(fixedDelay = 1000 * 60)
    public void updateSeatAndTicketState() {

        //현재 사용중인 전체 좌석에 대해 상태 업데이트를 진행한다. (seat, reservation, ticket)
        int count = seatService.updateAllReservedSeatState();
        log.debug("update All Seat State. count = {}", count);

        //Fixed Ticket 상태 업데이트를 진행한다.
        log.debug("update All Ticket State");
        Long updateCount = ticketService.updateAllValidTicketState();
        log.info("update Ticket. count={}", updateCount);
    }

    @Scheduled(fixedDelay = 1000 * 60)
        public void alertFCMAlmostFinishedSeat() throws IOException {
        log.debug("alert almost finished seat");
        seatService.alertAlmostFinishedSeat();
    }
}
