package asc.portfolio.ascSb.seat.controller;

import asc.portfolio.ascSb.seatreservationinfo.domain.SeatReservationInfo;
import asc.portfolio.ascSb.common.auth.LoginUser;
import asc.portfolio.ascSb.seatreservationinfo.service.SeatReservationInfoService;
import asc.portfolio.ascSb.seat.dto.SeatResponseDto;
import asc.portfolio.ascSb.seat.service.SeatService;
import asc.portfolio.ascSb.user.service.UserRoleCheckService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/seat")
public class SeatController {

    private final SeatService seatService;
    private final UserRoleCheckService userRoleCheckService;
    private final SeatReservationInfoService seatReservationInfoService;

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<String> nullPointExHandle(NullPointerException ex) {
        log.info("NullPointerException ex", ex);
        return new ResponseEntity<>("Null Exception", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> illegalArgumentExHandle(IllegalArgumentException ex) {
        log.info("IllegalArgumentException ex", ex);
        return new ResponseEntity<>("IllegalArgument Exception", HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/my-seat")
    public ResponseEntity<SeatResponseDto> seatStateOne(@LoginUser Long userId) {
        return new ResponseEntity<>(seatService.getMySeatStatus(userId), HttpStatus.OK);
    }

    @PostMapping("/reservation/")
    public ResponseEntity<String> reserveSeat(@LoginUser Long userId, @RequestParam("seat") Integer seatNumber,
                                              @RequestParam("time") Long startTime) {
        Long checkTime = startTime;

        // 프론트에서 보내는 startTime이 0이면 => 자리 교체 요청 => 사용중인 좌석을 찾아서 그 값을 startTime에 대입
        if(startTime == 0) {
             SeatReservationInfo validSeatRezInfo = seatReservationInfoService.validUserSeatReservationInfo(userId);
             checkTime = validSeatRezInfo.getStartTime() - validSeatRezInfo.updateTimeInUse();
        }

        Boolean isSuccess = seatService.reserveSeat(userId, seatNumber, checkTime);
        if (!isSuccess) {
            return new ResponseEntity<>("Failed", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }

    @PostMapping("/exit")
    public ResponseEntity<String> exitSeat(@LoginUser Long userId) {

        Boolean isSuccess = seatService.exitSeat(userId);
        if (!isSuccess) {
            return new ResponseEntity<>("No seat where the user sat", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }

    @PostMapping("/exit-admin/{seatNumber}")
    public ResponseEntity<String> exitSeat(@LoginUser Long adminId, @PathVariable int seatNumber) {
        userRoleCheckService.isAdmin(adminId);
        seatService.exitSeatBySeatNumber(adminId, seatNumber);
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }
}
