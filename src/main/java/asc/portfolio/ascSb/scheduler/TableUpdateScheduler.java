package asc.portfolio.ascSb.scheduler;

import asc.portfolio.ascSb.ticket.domain.Ticket;
import asc.portfolio.ascSb.expiredticket.service.ExpiredTicketService;
import asc.portfolio.ascSb.seat.service.SeatService;
import asc.portfolio.ascSb.ticket.service.TicketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Slf4j
@Component
@Profile("!test") // test 프로필이 아닌 경우에만 활성화
@RequiredArgsConstructor
public class TableUpdateScheduler {

    private final SeatService seatService;
    private final TicketService ticketService;
    private final ExpiredTicketService expiredTicketService;

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

    @Scheduled(cron = "30 50 23 * * *") // 30일 간격 은행 점검 시간인 23시 50분 마다 갱신
    public void moveToExpiredTicket() {
        // ticket 테이블 전체에서 invalid 상태의 티켓을 뽑아온다
        List<Ticket> invalidTicketList = ticketService.allInvalidTicketInfo();
        log.debug("Find all invalid ticket list");
        // invalid 상태의 티켓들을 EXPIRED_TICKET 테이블에 옮긴다
        boolean isSuccessTransfer = expiredTicketService.transferInvalidTicket(invalidTicketList);
        log.debug("Move invalid ticket list to expired table");

        // invalid 상태의 티켓들을 테이블에서 제거
        if(isSuccessTransfer) {
            ticketService.deleteInvalidTicket(invalidTicketList);
            // log.debug("Size of deleted invalid ticket = {}", invalidTicketList.size());
        }
    }
}
