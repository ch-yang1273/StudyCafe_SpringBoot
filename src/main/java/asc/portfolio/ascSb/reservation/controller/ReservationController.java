package asc.portfolio.ascSb.reservation.controller;

import asc.portfolio.ascSb.common.auth.LoginUser;
import asc.portfolio.ascSb.reservation.dto.CreateReservationRequest;
import asc.portfolio.ascSb.reservation.dto.ReleaseReservationRequest;
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
    public ResponseEntity<ReservationResponse> getMyReservation(@LoginUser Long userId) {
        return new ResponseEntity<>(reservationService.getReservation(userId), HttpStatus.OK);
    }

    @PostMapping("/reserve")
    public ResponseEntity<Void> createReservation(@LoginUser Long userId, @RequestBody CreateReservationRequest dto) {
        reservationService.createReservation(userId, dto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/release")
    public ResponseEntity<Void> releaseReservation(@LoginUser Long userId) {
        reservationService.releaseReservation(userId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/admin/release")
    public ResponseEntity<Void> releaseReservationByAdmin(
            @LoginUser Long adminId,
            @RequestBody ReleaseReservationRequest dto) {
        reservationService.releaseReservationByAdmin(adminId, dto.getCafeId(), dto.getSeatId());
        return ResponseEntity.ok().build();
    }
}
