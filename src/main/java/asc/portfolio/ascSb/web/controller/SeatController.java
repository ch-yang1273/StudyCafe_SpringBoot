package asc.portfolio.ascSb.web.controller;

import asc.portfolio.ascSb.domain.seatreservationinfo.SeatReservationInfo;
import asc.portfolio.ascSb.domain.user.User;
import asc.portfolio.ascSb.domain.user.UserRoleType;
import asc.portfolio.ascSb.jwt.LoginUser;
import asc.portfolio.ascSb.service.seatreservationinfo.SeatReservationInfoService;
import asc.portfolio.ascSb.web.dto.seat.SeatResponseDto;
import asc.portfolio.ascSb.web.dto.seat.SeatSelectResponseDto;
import asc.portfolio.ascSb.service.seat.SeatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/seat")
public class SeatController {

    private final SeatService seatService;

    private final SeatReservationInfoService seatReservationInfoService;

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<String> nullPointExHandle(NullPointerException ex) {
        log.info("NullPointerException ex", ex);
        return new ResponseEntity<>("Bad Request", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> illegalArgumentExHandle(IllegalArgumentException ex) {
        log.info("IllegalArgumentException ex", ex);
        return new ResponseEntity<>("Bad Request", HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/{cafeName}")
    public ResponseEntity<List<SeatSelectResponseDto>> seatStateList(@PathVariable String cafeName) {
        if(cafeName.isEmpty()) {
            log.info("cafeName이 비어 있습니다.");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(seatService.showCurrentAllSeatState(cafeName), HttpStatus.OK);
    }

    @GetMapping("/one")
    public ResponseEntity<SeatResponseDto> seatStateOne(@LoginUser User user) {

        // 유저가 사용중인 seatNumber
        int seatNumber = seatReservationInfoService.validUserSeatReservationInfo(user).getSeatNumber();

        return new ResponseEntity<>(seatService.showSeatStateOne(user.getCafe().getCafeName(), seatNumber), HttpStatus.OK);
    }

    @PostMapping("/reservation/")
    public ResponseEntity<String> reserveSeat(@LoginUser User user, @RequestParam("seat") Integer seatNumber, @RequestParam("time") Long startTime) {

        Long checkTime = startTime;

        //선택 된 카페가 없음.
        if (user.getCafe() == null) {
            return new ResponseEntity<>("Select a cafe first", HttpStatus.BAD_REQUEST);
        }

        // 프론트에서 보내는 startTime이 0이면 => 자리 교체 요청 => 사용중인 좌석을 찾아서 그 값을 startTime에 대입
        if(startTime == 0) {
             SeatReservationInfo validSeat = seatReservationInfoService.validUserSeatReservationInfo(user);
             checkTime = validSeat.getStartTime() - validSeat.getTimeInUse();
        }

        Boolean isSuccess = seatService.reserveSeat(user, seatNumber, checkTime);
        if (!isSuccess) {
            return new ResponseEntity<>("Failed", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }

    @PostMapping("/exit")
    public ResponseEntity<String> exitSeat(@LoginUser User user) {

        Boolean isSuccess = seatService.exitSeat(user);
        if (!isSuccess) {
            return new ResponseEntity<>("No seat where the user sat", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }

    @PostMapping("/exit-admin/{seatNumber}")
    public ResponseEntity<String> exitSeat(@LoginUser User admin, @PathVariable int seatNumber) {
        if (admin.getRole() == UserRoleType.ADMIN) {
            if (admin.getCafe() != null) {
                log.info("Exit Seat By Admin = {}", admin.getLoginId());
                seatService.exitSeatBySeatNumber(admin.getCafe(), seatNumber);
                return new ResponseEntity<>("Success", HttpStatus.OK);
            } else {
                log.warn("Set up a cafe field first");
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            }

        } else {
            log.info("Unauthorized User");
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
    }
}
