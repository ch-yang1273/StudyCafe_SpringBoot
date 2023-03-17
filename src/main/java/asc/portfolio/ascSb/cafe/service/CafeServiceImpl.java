package asc.portfolio.ascSb.cafe.service;

import asc.portfolio.ascSb.cafe.domain.Cafe;
import asc.portfolio.ascSb.cafe.domain.CafeRepository;
import asc.portfolio.ascSb.user.domain.User;
import asc.portfolio.ascSb.cafe.dto.CafeResponseDto;
import asc.portfolio.ascSb.user.domain.UserRepository;
import asc.portfolio.ascSb.user.exception.UnknownUserException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CafeServiceImpl implements CafeService {

  private final CafeRepository cafeRepository;

  private final UserRepository userRepository;

  @Override
  public List<CafeResponseDto> showAllCafeList() {
    return cafeRepository.findAllCafeNameAndArea();
  }

  @Override
  public String changeReservedUserCafe(Long userId, String cafeName) {
    User user = userRepository.findById(userId).orElseThrow(() -> new UnknownUserException());
    Optional<Cafe> findCafeOpt = cafeRepository.findByCafeName(cafeName);

    if (findCafeOpt.isEmpty()) {
      log.error("Unknown Cafe Name = {}", cafeName);
      return null;
    } else {
      findCafeOpt.ifPresent(c -> user.changeCafe(c));
      return findCafeOpt.get().getCafeName();
    }
  }
}
