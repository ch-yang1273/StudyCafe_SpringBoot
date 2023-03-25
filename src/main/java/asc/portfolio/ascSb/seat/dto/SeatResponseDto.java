package asc.portfolio.ascSb.seat.dto;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class SeatResponseDto {

    //From Seat
    private Integer seatNumber;
    private boolean isReserved;

    //Form reservation
    private LocalDateTime startTime;
    private Long timeInUse;

    //From Ticket
    private LocalDateTime fixedTermTicket;
    private Long partTimeTicket;
    private Long remainingTime;

    @Builder
    private SeatResponseDto(Integer seatNumber, boolean isReserved, LocalDateTime startTime, Long timeInUse, LocalDateTime fixedTermTicket, Long partTimeTicket, Long remainingTime) {
        this.seatNumber = seatNumber;
        this.isReserved = isReserved;
        this.startTime = startTime;
        this.timeInUse = timeInUse;
        this.fixedTermTicket = fixedTermTicket;
        this.partTimeTicket = partTimeTicket;
        this.remainingTime = remainingTime;
    }

    //정적 팩토리 매세드
    public static SeatResponseDto createUnReservedSeatDto(Integer seatNumber) {
        return SeatResponseDto.builder()
                .seatNumber(seatNumber)
                .isReserved(false)
                .build();
    }

    //정적 팩토리 메서드
    public static SeatResponseDto setFixedTermSeat(Integer seatNumber, LocalDateTime startTime, Long timeInUse, LocalDateTime fixedTermTicket) {
        return SeatResponseDto.builder()
                .seatNumber(seatNumber)
                .isReserved(true)
                .startTime(startTime)
                .timeInUse(timeInUse)
                .fixedTermTicket(fixedTermTicket)
                .build();
    }

    //정적 팩토리 메서드
    public static SeatResponseDto setPartTimeSeat(Integer seatNumber, LocalDateTime startTime, Long timeInUse, Long partTimeTicket, Long remainingTime) {
        return SeatResponseDto.builder()
                .seatNumber(seatNumber)
                .isReserved(true)
                .startTime(startTime)
                .timeInUse(timeInUse)
                .partTimeTicket(partTimeTicket)
                .remainingTime(remainingTime)
                .build();
    }
}
