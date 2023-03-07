package asc.portfolio.ascSb.cafe.domain;

import asc.portfolio.ascSb.cafe.dto.CafeResponseDto;

import java.util.List;

public interface CafeCustomRepository {

  public List<CafeResponseDto> findAllCafeNameAndArea();
}
