package asc.portfolio.ascSb.seatreservationinfo.service;

import asc.portfolio.ascSb.cafe.domain.Cafe;
import asc.portfolio.ascSb.follow.domain.FollowFinder;
import asc.portfolio.ascSb.seatreservationinfo.domain.SeatReservationInfo;
import asc.portfolio.ascSb.seatreservationinfo.domain.SeatReservationInfoRepository;
import asc.portfolio.ascSb.seatreservationinfo.domain.SeatReservationInfoStateType;
import asc.portfolio.ascSb.user.domain.User;
import asc.portfolio.ascSb.seatreservationinfo.dto.SeatReservationInfoSelectResponseDto;
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
public class SeatReservationInfoServiceImpl implements SeatReservationInfoService  {

    private final SeatReservationInfoRepository seatReservationInfoRepository;
    private final UserFinder userFinder;
    private final FollowFinder followFinder;

    @Override
    public SeatReservationInfoSelectResponseDto showUserSeatReservationInfo(Long userId) {
        User user = userFinder.findById(userId);
        //todo : 삭제. cafe는 Seat에서 나와야지 Follow에서 나오면 안된다.
        Cafe cafe = followFinder.findFollowedCafe(userId);

        try {
            SeatReservationInfoSelectResponseDto dto = seatReservationInfoRepository.findSeatInfoByUserIdAndCafeName(user.getLoginId(), cafe.getCafeName());
            dto.setPeriod(dto.getCreateDate().format(DateTimeFormatter.ofPattern("yyyyMMddHHmm")));
            return dto;
        } catch (Exception exception) {
            log.debug("존재하지 않는 SeatReservationInfo 입니다.");
        }
        return null;
    }

    @Override
    public SeatReservationInfo validUserSeatReservationInfo(Long userId) {
        User user = userFinder.findById(userId);
        //todo : 삭제. cafe는 Seat에서 나와야지 Follow에서 나오면 안된다.
        Cafe cafe = followFinder.findFollowedCafe(userId);
        return seatReservationInfoRepository.findByUserLoginIdAndIsValidAndCafeName(
                user.getLoginId(), SeatReservationInfoStateType.VALID, cafe.getCafeName());
    }
}
