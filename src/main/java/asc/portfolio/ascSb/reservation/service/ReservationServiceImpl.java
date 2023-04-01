package asc.portfolio.ascSb.reservation.service;

import asc.portfolio.ascSb.cafe.domain.CafeFinder;
import asc.portfolio.ascSb.common.domain.CurrentTimeProvider;
import asc.portfolio.ascSb.reservation.domain.Reservation;
import asc.portfolio.ascSb.reservation.domain.ReservationFinder;
import asc.portfolio.ascSb.reservation.domain.ReservationManager;
import asc.portfolio.ascSb.reservation.domain.ReservationValidation;
import asc.portfolio.ascSb.reservation.dto.CreateReservationRequest;
import asc.portfolio.ascSb.seat.domain.SeatFinder;
import asc.portfolio.ascSb.reservation.dto.ReservationResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReservationServiceImpl implements ReservationService {

    private final ReservationValidation reservationValidation;
    private final ReservationManager reservationManager;

    private final ReservationFinder reservationFinder;
    private final CafeFinder cafeFinder;
    private final SeatFinder seatFinder;

    private final CurrentTimeProvider currentTimeProvider;

    @Transactional(readOnly = true)
    @Override
    public ReservationResponse getReservation(Long userId) {
        Reservation reservation = reservationFinder.findByUserIdAndInUseStatus(userId);
        return ReservationResponse.of(reservation);
    }

    @Transactional
    @Override
    public void createReservation(Long userId, CreateReservationRequest dto) {
        Reservation reservation = dto.toEntity(userId, currentTimeProvider.localDateTimeNow());
        reservationValidation.validate(reservation);
        reservationManager.reserve(reservation);
    }

    @Transactional
    @Override
    public void releaseReservation(Long userId) {
        Reservation reservation = reservationFinder.findByUserIdAndInUseStatus(userId);
        reservationManager.finish(userId, reservation);
    }

    @Transactional
    @Override
    public void releaseReservationByAdmin(Long adminId, Long seatId) {
        Reservation reservation = reservationFinder.findBySeatIdAndInUseStatus(seatId);
        reservationManager.finish(adminId, reservation);
    }
}
