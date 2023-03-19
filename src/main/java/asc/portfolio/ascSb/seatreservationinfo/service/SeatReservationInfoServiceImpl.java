package asc.portfolio.ascSb.seatreservationinfo.service;

import asc.portfolio.ascSb.cafe.domain.Cafe;
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

    @Override
    public SeatReservationInfoSelectResponseDto showUserSeatReservationInfo(Long userId) {
        User user = userFinder.findById(userId);
        Cafe cafe = user.getCafe();

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
        return seatReservationInfoRepository.findByUserLoginIdAndIsValidAndCafeName(
                user.getLoginId(), SeatReservationInfoStateType.VALID, user.getCafe().getCafeName());
    }
}
