package asc.portfolio.ascSb.reservation.domain;

import asc.portfolio.ascSb.common.domain.CurrentTimeProvider;
import asc.portfolio.ascSb.seat.domain.Seat;
import asc.portfolio.ascSb.seat.domain.SeatFinder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ReservationManager {

    private final ReservationRepository reservationRepository;
    private final SeatFinder seatFinder;

    private final CurrentTimeProvider currentTimeProvider;

    public void reserve(Reservation rez) {
        Seat seat = seatFinder.findById(rez.getSeatId());

        reservationRepository.save(rez);
        seat.reserveSeat(rez.toUsageData());
    }

    public void release(Reservation rez) {
        Seat seat = seatFinder.findById(rez.getSeatId());
        rez.release(currentTimeProvider.localDateTimeNow());
        seat.exitSeat();
    }
}
