package asc.portfolio.ascSb.cafe.controller;

import asc.portfolio.ascSb.cafe.dto.CafeFollowersResponse;
import asc.portfolio.ascSb.cafe.dto.CafeRegistrationRequest;
import asc.portfolio.ascSb.cafe.dto.SeatStatusResponse;
import asc.portfolio.ascSb.cafe.service.CafeService;
import asc.portfolio.ascSb.cafe.dto.CafeResponse;
import asc.portfolio.ascSb.cafe.service.FollowingService;
import asc.portfolio.ascSb.common.auth.LoginUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/cafe")
public class CafeController {

    private final CafeService cafeService;
    private final FollowingService followingService;

    @GetMapping("/list")
    public Page<CafeResponse> getCafeList(Pageable pageable) {
        return cafeService.getCafeList(pageable);
    }

    @PostMapping("/register")
    public ResponseEntity<Void> registerCafe(
            @LoginUser Long adminId,
            @RequestBody @Valid CafeRegistrationRequest dto) {
        cafeService.registerCafe(adminId, dto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/info")
    public ResponseEntity<CafeResponse> getMyCafe(@LoginUser Long adminId) {
        return ResponseEntity.ok().body(cafeService.findByAdminId(adminId));
    }

    @PostMapping("/{cafeId}/open")
    public ResponseEntity<Void> openCafe(@LoginUser Long adminId, @PathVariable Long cafeId) {
        cafeService.openCafe(adminId, cafeId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{cafeId}/close")
    public ResponseEntity<Void> closeCafe(@LoginUser Long adminId, @PathVariable Long cafeId) {
        cafeService.closeCafe(adminId, cafeId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{cafeId}/seats")
    public ResponseEntity<List<SeatStatusResponse>> getAllSeatsByCafeId(@PathVariable Long cafeId) {
        return new ResponseEntity<>(cafeService.getAllSeatsByCafeId(cafeId), HttpStatus.OK);
    }

    @PostMapping("/{cafeId}/follow")
    public ResponseEntity<Void> followCafe(@LoginUser Long userId, @PathVariable Long cafeId) {
        followingService.follow(userId, cafeId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{cafeId}/unfollow")
    public ResponseEntity<Void> unfollowCafe(@LoginUser Long userId, @PathVariable Long cafeId) {
        followingService.unfollow(userId, cafeId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{cafeId}/followers")
    public ResponseEntity<CafeFollowersResponse> followers(@PathVariable Long cafeId) {
        return ResponseEntity.ok().body(followingService.getFollowers(cafeId));
    }
}
