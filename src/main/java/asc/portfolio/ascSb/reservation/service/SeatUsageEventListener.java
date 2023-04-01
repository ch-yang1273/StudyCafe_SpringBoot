package asc.portfolio.ascSb.reservation.service;

import asc.portfolio.ascSb.reservation.domain.Reservation;
import asc.portfolio.ascSb.reservation.domain.ReservationFinder;
import asc.portfolio.ascSb.reservation.domain.ReservationManager;
import asc.portfolio.ascSb.seat.domain.Seat;
import asc.portfolio.ascSb.seat.domain.SeatFinder;
import asc.portfolio.ascSb.seat.dto.SeatUsageEndingSoonEvent;
import asc.portfolio.ascSb.seat.dto.SeatUsageTerminatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionalEventListener;

@RequiredArgsConstructor
@Service
public class SeatUsageEventListener {

    private SeatFinder seatFinder;
    private ReservationFinder reservationFinder;
    private ReservationManager reservationManager;

    @TransactionalEventListener(SeatUsageEndingSoonEvent.class)
    public void handleEndingSoonEvent(SeatUsageEndingSoonEvent event) {
        //todo : FCM Alert
    }

    @TransactionalEventListener(SeatUsageTerminatedEvent.class)
    public void handlerTerminatedEvent(SeatUsageTerminatedEvent event) {
        Long seatId = event.getSeatId();

        Seat seat = seatFinder.findById(seatId);
        Reservation reservation = reservationFinder.findBySeatIdAndInUseStatus(seatId);

        reservationManager.finish(seat.getUserId(), reservation);
    }
}
