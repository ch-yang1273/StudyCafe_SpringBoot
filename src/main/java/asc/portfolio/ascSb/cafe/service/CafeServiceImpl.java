package asc.portfolio.ascSb.cafe.service;

import asc.portfolio.ascSb.cafe.domain.Cafe;
import asc.portfolio.ascSb.cafe.domain.CafeRepository;
import asc.portfolio.ascSb.cafe.dto.SeatStatusDto;
import asc.portfolio.ascSb.cafe.exception.CafeNotFoundException;
import asc.portfolio.ascSb.seat.domain.SeatRepository;
import asc.portfolio.ascSb.user.domain.User;
import asc.portfolio.ascSb.cafe.dto.CafeResponseDto;
import asc.portfolio.ascSb.user.domain.UserRepository;
import asc.portfolio.ascSb.user.exception.UnknownUserException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CafeServiceImpl implements CafeService {

    private final CafeRepository cafeRepository;

    // todo : 패키지 순환 발생 Seat -> Cafe , CafeService -> SeatRepository
    private final SeatRepository seatRepository;

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    @Override
    public Page<CafeResponseDto> getCafeList(Pageable pageable) {
        return cafeRepository.findAll(pageable)
                .map(cafe -> new CafeResponseDto(cafe.getId(), cafe.getCafeName(), cafe.getCafeArea()));
    }

    @Transactional(readOnly = true)
    @Override
    public List<SeatStatusDto> getAllSeatsByCafeId(Long cafeId) {
        Cafe cafe = cafeRepository.findById(cafeId).orElseThrow(() -> new CafeNotFoundException());
        return seatRepository.findByCafe(cafe).stream()
                .map(seat -> new SeatStatusDto(seat.getId(), seat.isReserved()))
                .collect(Collectors.toList());
    }

    @Transactional
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
