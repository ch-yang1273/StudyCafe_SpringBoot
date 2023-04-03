package asc.portfolio.ascSb.seat.service;
import asc.portfolio.ascSb.cafe.domain.Cafe;
import asc.portfolio.ascSb.cafe.domain.CafeFinder;
import asc.portfolio.ascSb.common.domain.CurrentTimeProvider;
import asc.portfolio.ascSb.seat.domain.Seat;
import asc.portfolio.ascSb.seat.domain.SeatFinder;
import asc.portfolio.ascSb.seat.dto.SeatStatusResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class SeatServiceImpl implements SeatService {

    private final SeatFinder seatFinder;
    private final CafeFinder cafeFinder;

    private final CurrentTimeProvider currentTimeProvider;

    @Transactional(readOnly = true)
    @Override
    public SeatStatusResponse getSeatStatus(Long userId) {
        Seat seat = seatFinder.findByUserId(userId);
        Cafe cafe = cafeFinder.findById(seat.getCafeId());

        return new SeatStatusResponse(cafe, seat, currentTimeProvider.localDateTimeNow());
    }

    @Transactional(readOnly = true)
    @Override
    public List<SeatStatusResponse> getAllSeatsByCafeId(Long cafeId) {
        Cafe cafe = cafeFinder.findById(cafeId);

        List<Seat> seats = seatFinder.findAllByCafeId(cafeId);
        return seats.stream()
                .map(seat -> new SeatStatusResponse(cafe, seat, currentTimeProvider.localDateTimeNow()))
                .collect(Collectors.toList());
    }
}
