package asc.portfolio.ascSb.reservation.service;

import asc.portfolio.ascSb.common.domain.CurrentTimeProvider;
import asc.portfolio.ascSb.reservation.domain.Reservation;
import asc.portfolio.ascSb.reservation.domain.ReservationFinder;
import asc.portfolio.ascSb.reservation.domain.ReservationLifecycleManager;
import asc.portfolio.ascSb.reservation.dto.CreateReservationRequest;
import asc.portfolio.ascSb.reservation.dto.ReservationResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReservationServiceImpl implements ReservationService {

    private final ReservationLifecycleManager reservationLifecycleManager;

    private final ReservationFinder reservationFinder;

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
        LocalDateTime now = currentTimeProvider.localDateTimeNow();
        reservationLifecycleManager.reserve(userId, dto.getCafeId(), dto.getSeatId(), dto.getTickerId(), now);
    }

    @Transactional
    @Override
    public void releaseReservation(Long userId) {
        Reservation reservation = reservationFinder.findByUserIdAndInUseStatus(userId);
        reservationLifecycleManager.finish(userId, reservation, currentTimeProvider.localDateTimeNow());
    }

    @Transactional
    @Override
    public void releaseReservationByAdmin(Long adminId, Long seatId) {
        Reservation reservation = reservationFinder.findBySeatIdAndInUseStatus(seatId);
        reservationLifecycleManager.finish(adminId, reservation, currentTimeProvider.localDateTimeNow());
    }
}
