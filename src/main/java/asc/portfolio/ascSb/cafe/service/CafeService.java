package asc.portfolio.ascSb.cafe.service;

import asc.portfolio.ascSb.cafe.dto.CafeResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CafeService {

    public Page<CafeResponseDto> getCafeList(Pageable pageable);

    public String changeReservedUserCafe(Long userId, String cafeName);
}
