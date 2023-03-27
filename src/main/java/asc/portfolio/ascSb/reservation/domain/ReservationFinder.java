package asc.portfolio.ascSb.reservation.domain;

import asc.portfolio.ascSb.reservation.exception.ReservationErrorData;
import asc.portfolio.ascSb.reservation.exception.ReservationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class ReservationFinder {

    private final ReservationRepository reservationRepository;

    public Reservation findById(Long reservationId) {
        return reservationRepository.findById(reservationId).orElseThrow(
                () -> new ReservationException(ReservationErrorData.RESERVATION_NOT_FOUND));
    }

    public List<Reservation> findListByUserIdAndStatus(Long userId, ReservationStatus status) {
        return reservationRepository.findListByUserIdAndStatus(userId, status);
    }

    public Reservation findByUserIdAndStatus(Long userId, ReservationStatus isValid) {
        return reservationRepository.findByUserIdAndStatus(userId, isValid).orElseThrow(
                () -> new ReservationException(ReservationErrorData.RESERVATION_NOT_FOUND));
    }

    public List<Reservation> findListBySeatIdAndStatus(Long seatId, ReservationStatus status) {
        return reservationRepository.findListBySeatIdAndStatus(seatId, status);
    }
}
