package asc.portfolio.ascSb.cafe.service;

import asc.portfolio.ascSb.cafe.dto.CafeResponseDto;
import asc.portfolio.ascSb.cafe.dto.SeatStatusDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CafeService {

    Page<CafeResponseDto> getCafeList(Pageable pageable);

    List<SeatStatusDto> getAllSeatsByCafeId(Long cafeId);
}
