package asc.portfolio.ascSb.seat.domain;

import asc.portfolio.ascSb.seat.exception.SeatErrorData;
import asc.portfolio.ascSb.seat.exception.SeatException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Component
public class SeatFinder {

    private final SeatRepository seatRepository;

    public Seat findById(Long seatId) {
        return seatRepository.findById(seatId).orElseThrow(() -> new SeatException(SeatErrorData.SEAT_NOT_FOUND));
    }

    private Optional<Seat> findOptionalByUserId(Long userId) {
        return seatRepository.findOptionalByUserId(userId);
    }

    public Seat findByUserId(Long userId) {
        return findOptionalByUserId(userId).orElseThrow(() -> new SeatException(SeatErrorData.SEAT_NOT_FOUND));
    }

    public boolean hasReservedSeat(Long userId) {
        return findOptionalByUserId(userId).isPresent();
    }

    public List<Seat> findAllByCafeId(Long cafeId) {
        return seatRepository.findAllByCafeId(cafeId);
    }
}
