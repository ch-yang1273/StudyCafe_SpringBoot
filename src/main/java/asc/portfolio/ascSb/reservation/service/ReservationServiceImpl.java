package asc.portfolio.ascSb.reservation.service;

import asc.portfolio.ascSb.cafe.domain.Cafe;
import asc.portfolio.ascSb.follow.domain.FollowFinder;
import asc.portfolio.ascSb.reservation.domain.Reservation;
import asc.portfolio.ascSb.reservation.domain.ReservationRepository;
import asc.portfolio.ascSb.reservation.domain.ReservationStatus;
import asc.portfolio.ascSb.user.domain.User;
import asc.portfolio.ascSb.reservation.dto.ReservationResponse;
import asc.portfolio.ascSb.user.domain.UserFinder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;


@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final UserFinder userFinder;
    private final FollowFinder followFinder;

    @Override
    public ReservationResponse getReservation(Long userId) {
        User user = userFinder.findById(userId);
        //todo : 삭제. cafe는 Seat에서 나와야지 Follow에서 나오면 안된다.
        Cafe cafe = followFinder.findFollowedCafe(userId);

        try {
            ReservationResponse dto = reservationRepository.findReservationByUserIdAndCafeName(user.getId(), cafe.getId());
            dto.setPeriod(dto.getCreateDate().format(DateTimeFormatter.ofPattern("yyyyMMddHHmm")));
            return dto;
        } catch (Exception exception) {
            log.debug("존재하지 않는 reservation 입니다.");
        }
        return null;
    }

    @Override
    public Reservation getValidReservation(Long userId) {
        User user = userFinder.findById(userId);
        //todo : 삭제. cafe는 Seat에서 나와야지 Follow에서 나오면 안된다.
        Cafe cafe = followFinder.findFollowedCafe(userId);
        return reservationRepository.findByUserIdAndStatusAndCafeId(
                user.getId(), ReservationStatus.IN_USE, cafe.getId());
    }
}
