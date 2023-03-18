package asc.portfolio.ascSb.cafe.service;

import asc.portfolio.ascSb.cafe.dto.CafeResponseDto;

import java.util.List;

public interface CafeService {

  public List<CafeResponseDto> showAllCafeList();

  public String changeReservedUserCafe(Long userId, String cafeName);
}
