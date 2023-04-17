package asc.portfolio.ascSb.reservation.service;

import asc.portfolio.ascSb.common.domain.CurrentTimeProvider;
import asc.portfolio.ascSb.reservation.domain.Reservation;
import asc.portfolio.ascSb.reservation.domain.ReservationFinder;
import asc.portfolio.ascSb.reservation.domain.ReservationLifecycleManager;
import asc.portfolio.ascSb.seat.domain.Seat;
import asc.portfolio.ascSb.seat.domain.SeatFinder;
import asc.portfolio.ascSb.seat.dto.SeatUsageEndingSoonEvent;
import asc.portfolio.ascSb.seat.dto.SeatUsageTerminatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@RequiredArgsConstructor
@Service
public class SeatUsageEventListener {

    private SeatFinder seatFinder;
    private ReservationFinder reservationFinder;
    private ReservationLifecycleManager reservationLifecycleManager;
    private CurrentTimeProvider currentTimeProvider;

    @EventListener
    public void handleEndingSoonEvent(SeatUsageEndingSoonEvent event) {
        log.info("Handling SeatUsageEndingSoonEvent: SeatId={}, TimeStamp={}", event.getSeatId(), event.getTimestamp());
        //todo : FCM Alert
    }

    @TransactionalEventListener(SeatUsageTerminatedEvent.class)
    public void handlerTerminatedEvent(SeatUsageTerminatedEvent event) {
        Long seatId = event.getSeatId();

        Seat seat = seatFinder.findById(seatId);
        Reservation reservation = reservationFinder.findBySeatIdAndInUseStatus(seatId);

        reservationLifecycleManager.finish(seat.getUserId(), reservation, currentTimeProvider.localDateTimeNow());
    }
}
