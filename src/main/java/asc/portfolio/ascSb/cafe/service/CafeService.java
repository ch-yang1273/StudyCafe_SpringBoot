package asc.portfolio.ascSb.cafe.service;

import asc.portfolio.ascSb.cafe.dto.CafeRegistrationRequest;
import asc.portfolio.ascSb.cafe.dto.CafeResponse;
import asc.portfolio.ascSb.cafe.dto.SeatStatusResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CafeService {

    Page<CafeResponse> getCafeList(Pageable pageable);

    List<SeatStatusResponse> getAllSeatsByCafeId(Long cafeId);

    void openCafe(Long adminId, Long cafeId);

    void closeCafe(Long adminId, Long cafeId);

    CafeResponse findByAdminId(Long adminId);

    void registerCafe(Long adminId, CafeRegistrationRequest dto);
}
