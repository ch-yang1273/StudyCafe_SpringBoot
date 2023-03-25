package asc.portfolio.ascSb.seat.controller;

import asc.portfolio.ascSb.common.auth.LoginUser;
import asc.portfolio.ascSb.reservation.service.ReservationService;
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
    private final ReservationService reservationService;

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

    @PostMapping("/reservation")
    public ResponseEntity<String> reserveSeat(@LoginUser Long userId, @RequestParam("seat") Integer seatNumber) {
        //todo startTime 안받음, RequestParam이 아니라 Dto 받는 것으로 수정
        // 사용 중인 좌석이 있으면 Error
        Long checkTime = 1L;

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
