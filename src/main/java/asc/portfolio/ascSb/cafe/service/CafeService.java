package asc.portfolio.ascSb.cafe.service;

import asc.portfolio.ascSb.user.domain.User;
import asc.portfolio.ascSb.cafe.dto.CafeResponseDto;

import java.util.List;

public interface CafeService {

  public List<CafeResponseDto> showAllCafeList();

  public String changeReservedUserCafe(User user, String cafeName);
}
