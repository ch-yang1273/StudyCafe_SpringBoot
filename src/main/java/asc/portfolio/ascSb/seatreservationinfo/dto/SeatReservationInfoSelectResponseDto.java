package asc.portfolio.ascSb.seatreservationinfo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SeatReservationInfoSelectResponseDto {
    private Integer seatNumber;
    private Long startTime;
    private Long timeInUse;

    private LocalDateTime createDate;

    private String period;
}
