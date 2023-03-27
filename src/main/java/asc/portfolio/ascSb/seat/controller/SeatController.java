package asc.portfolio.ascSb.seat.controller;

import asc.portfolio.ascSb.common.auth.LoginUser;
import asc.portfolio.ascSb.seat.dto.SeatStatusResponse;
import asc.portfolio.ascSb.seat.service.SeatService;
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

    @GetMapping("/my")
    public ResponseEntity<SeatStatusResponse> getMySeatStatus(@LoginUser Long userId) {
        return new ResponseEntity<>(seatService.getSeatStatus(userId), HttpStatus.OK);
    }
}
