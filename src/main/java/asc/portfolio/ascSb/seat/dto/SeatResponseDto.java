package asc.portfolio.ascSb.seat.dto;
import asc.portfolio.ascSb.seat.domain.SeatStateType;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class SeatResponseDto {

    //From Seat
    private Integer seatNumber;
    private SeatStateType seatState;

    //Form SeatReservationInfo
    private Long startTime;
    private Long timeInUse;

    //From Ticket
    private LocalDateTime fixedTermTicket;
    private Long partTimeTicket;
    private Long remainingTime;

    @Builder
    private SeatResponseDto(Integer seatNumber, SeatStateType seatState, Long startTime, Long timeInUse, LocalDateTime fixedTermTicket, Long partTimeTicket, Long remainingTime) {
        this.seatNumber = seatNumber;
        this.seatState = seatState;
        this.startTime = startTime;
        this.timeInUse = timeInUse;
        this.fixedTermTicket = fixedTermTicket;
        this.partTimeTicket = partTimeTicket;
        this.remainingTime = remainingTime;
    }

    //정적 팩토리 매세드
    public static SeatResponseDto setUnReservedSeat(Integer seatNumber, SeatStateType seatState) {
        return SeatResponseDto.builder()
                .seatNumber(seatNumber)
                .seatState(seatState)
                .build();
    }

    //정적 팩토리 메서드
    public static SeatResponseDto setFixedTermSeat(Integer seatNumber, SeatStateType seatState, Long startTime, Long timeInUse, LocalDateTime fixedTermTicket) {
        return SeatResponseDto.builder()
                .seatNumber(seatNumber)
                .seatState(seatState)
                .startTime(startTime)
                .timeInUse(timeInUse)
                .fixedTermTicket(fixedTermTicket)
                .build();
    }

    //정적 팩토리 메서드
    public static SeatResponseDto setPartTimeSeat(Integer seatNumber, SeatStateType seatState, Long startTime, Long timeInUse, Long partTimeTicket, Long remainingTime) {
        return SeatResponseDto.builder()
                .seatNumber(seatNumber)
                .seatState(seatState)
                .startTime(startTime)
                .timeInUse(timeInUse)
                .partTimeTicket(partTimeTicket)
                .remainingTime(remainingTime)
                .build();
    }
}
