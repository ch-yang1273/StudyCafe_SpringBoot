package asc.portfolio.ascSb.seat.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class SeatSelectResponseDto {

    private Integer seatNumber;
    private boolean isReserved;
}