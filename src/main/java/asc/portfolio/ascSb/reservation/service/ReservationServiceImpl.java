package asc.portfolio.ascSb.reservation.service;

import asc.portfolio.ascSb.cafe.domain.Cafe;
import asc.portfolio.ascSb.cafe.domain.CafeFinder;
import asc.portfolio.ascSb.common.domain.CurrentTimeProvider;
import asc.portfolio.ascSb.reservation.domain.Reservation;
import asc.portfolio.ascSb.reservation.domain.ReservationFinder;
import asc.portfolio.ascSb.reservation.domain.ReservationManager;
import asc.portfolio.ascSb.reservation.domain.ReservationStatus;
import asc.portfolio.ascSb.reservation.domain.ReservationValidation;
import asc.portfolio.ascSb.reservation.dto.CreateReservationRequest;
import asc.portfolio.ascSb.seat.domain.Seat;
import asc.portfolio.ascSb.seat.domain.SeatFinder;
import asc.portfolio.ascSb.reservation.dto.ReservationResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

        List<Reservation> list = reservationFinder.findListByUserIdAndStatus(userId, ReservationStatus.IN_USE);
        if (list.size() > 1) {
            throw new RuntimeException("해당 유저의 예약에서 IN_USE 상태의 좌석이 2개 이상입니다. id=" + userId);
        }

        return ReservationResponse.of(list.get(0));
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
        List<Reservation> list = reservationFinder.findListByUserIdAndStatus(userId, ReservationStatus.IN_USE);
        for (Reservation rez : list) {
            reservationManager.release(rez);
        }
    }

    @Transactional
    @Override
    public void releaseReservationByAdmin(Long adminId, Long cafeId, Long seatId) {
        Cafe cafe = cafeFinder.findById(cafeId);
        Seat seat = seatFinder.findById(seatId);

        cafe.isAdminOrElseThrow(adminId);
        seat.isBelongToOrElseThrow(cafeId);

        List<Reservation> list = reservationFinder.findListBySeatIdAndStatus(seatId, ReservationStatus.IN_USE);
        for (Reservation rez : list) {
            reservationManager.release(rez);
        }
    }
}
