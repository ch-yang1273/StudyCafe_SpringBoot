package asc.portfolio.ascSb.cafe.controller;

import asc.portfolio.ascSb.cafe.dto.SeatStatusDto;
import asc.portfolio.ascSb.cafe.service.CafeService;
import asc.portfolio.ascSb.cafe.dto.CafeResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/cafe")
public class CafeController {

    private final CafeService cafeService;

    @GetMapping("/list")
    public Page<CafeResponseDto> getCafeList(Pageable pageable) {
        return cafeService.getCafeList(pageable);
    }

    @GetMapping("/{cafeId}/seats")
    public ResponseEntity<List<SeatStatusDto>> getAllSeatsByCafeId(@PathVariable Long cafeId) {
        return new ResponseEntity<>(cafeService.getAllSeatsByCafeId(cafeId), HttpStatus.OK);
    }
}
