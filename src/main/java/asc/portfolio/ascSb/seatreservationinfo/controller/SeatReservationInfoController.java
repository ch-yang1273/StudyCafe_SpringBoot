package asc.portfolio.ascSb.seatreservationinfo.controller;

import asc.portfolio.ascSb.common.auth.LoginUser;
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
    public ResponseEntity<SeatReservationInfoSelectResponseDto> userSeatReservationInfo(@LoginUser Long userId) {
        return new ResponseEntity<>(seatReservationInfoService.showUserSeatReservationInfo(userId), HttpStatus.OK);
    }
}
