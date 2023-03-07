package asc.portfolio.ascSb.seat.dto;


import asc.portfolio.ascSb.seat.domain.SeatStateType;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SeatSelectResponseDto {

    private Integer seatNumber;
    private SeatStateType seatState;

    public SeatSelectResponseDto(SeatSelectResponseDto entity) {
        this.seatNumber = entity.getSeatNumber();
        this.seatState = entity.getSeatState();
    }
}