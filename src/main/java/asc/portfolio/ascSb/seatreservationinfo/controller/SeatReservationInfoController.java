package asc.portfolio.ascSb.seatreservationinfo.controller;


import asc.portfolio.ascSb.user.domain.User;
import asc.portfolio.ascSb.common.auth.jwt.LoginUser;
import asc.portfolio.ascSb.seatreservationinfo.dto.SeatReservationInfoSelectResponseDto;
import asc.portfolio.ascSb.seatreservationinfo.service.SeatReservationInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/seatReservationInfo")
public class SeatReservationInfoController {

    private final SeatReservationInfoService seatReservationInfoService;
    @GetMapping("/")
    public ResponseEntity<SeatReservationInfoSelectResponseDto> userSeatReservationInfo(@LoginUser User user) {
        if(user == null) {
            log.error("유효하지 않은 사용자입니다. 다시 로그인 해주세요");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(seatReservationInfoService.showUserSeatReservationInfo(
                user.getLoginId(), user.getCafe().getCafeName()), HttpStatus.OK);
    }
}
