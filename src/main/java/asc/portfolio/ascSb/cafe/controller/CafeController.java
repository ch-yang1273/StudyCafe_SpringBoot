package asc.portfolio.ascSb.cafe.controller;

import asc.portfolio.ascSb.cafe.dto.CafeRegistrationRequest;
import asc.portfolio.ascSb.cafe.service.CafeService;
import asc.portfolio.ascSb.cafe.dto.CafeResponse;
import asc.portfolio.ascSb.common.auth.LoginUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/cafe")
public class CafeController {

    private final CafeService cafeService;

    @GetMapping("/list")
    public ResponseEntity<Page<CafeResponse>> getCafeList(Pageable pageable) {
        return ResponseEntity.ok().body(cafeService.getCafeList(pageable));
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
}
