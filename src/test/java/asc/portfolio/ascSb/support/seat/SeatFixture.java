package asc.portfolio.ascSb.support.seat;

import asc.portfolio.ascSb.seat.domain.Seat;
import asc.portfolio.ascSb.seat.domain.UsageData;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
public enum SeatFixture {

    SEAT_A(1L, 1),
    SEAT_B(2L, 2),
    SEAT_C(3L, 3),
    SEAT_D(4L, 4),
    SEAT_E(5L, 5);

    private final Long id;
    private final int seatNumber;

    SeatFixture(Long id, int seatNumber) {
        this.id = id;
        this.seatNumber = seatNumber;
    }

    public Seat toSeat(Long cafeId) {
        return Seat.builder()
                .id(id)
                .cafeId(cafeId)
                .seatNumber(seatNumber)
                .build();
    }

    public static List<Seat> getSeats(Long cafeId) {
        return Stream.of(SeatFixture.values())
                .map(seatFixture -> seatFixture.toSeat(cafeId))
                .collect(Collectors.toList());
    }

    public static UsageData makeUsageData(Long userId, LocalDateTime endTime) {
        return new UsageData(userId, null, null, endTime);
    }
}
