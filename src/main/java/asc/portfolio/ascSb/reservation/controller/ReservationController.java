package asc.portfolio.ascSb.reservation.controller;

import asc.portfolio.ascSb.common.auth.LoginUser;
import asc.portfolio.ascSb.reservation.dto.ReservationResponse;
import asc.portfolio.ascSb.reservation.service.ReservationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/reservation")
public class ReservationController {

    private final ReservationService reservationService;
    @GetMapping("/")
    public ResponseEntity<ReservationResponse> getReservation(@LoginUser Long userId) {
        return new ResponseEntity<>(reservationService.getReservation(userId), HttpStatus.OK);
    }
}
