package asc.portfolio.ascSb.reservation.domain;

import asc.portfolio.ascSb.reservation.exception.ReservationErrorData;
import asc.portfolio.ascSb.reservation.exception.ReservationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ReservationFinder {

    private final ReservationRepository reservationRepository;

    public Reservation findById(Long reservationId) {
        return reservationRepository.findById(reservationId).orElseThrow(
                () -> new ReservationException(ReservationErrorData.RESERVATION_NOT_FOUND));
    }

    public Reservation findByUserIdAndInUseStatus(Long userId) {
        return reservationRepository.findByUserIdAndInUseStatus(userId).orElseThrow(
                () -> new ReservationException(ReservationErrorData.RESERVATION_NOT_FOUND));
    }

    public Reservation findBySeatIdAndInUseStatus(Long seatId) {
        return reservationRepository.findBySeatIdAndInUseStatus(seatId).orElseThrow(
                () -> new ReservationException(ReservationErrorData.RESERVATION_NOT_FOUND));
    }

}
