package asc.portfolio.ascSb.reservation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ReservationResponse {
    private Integer seatNumber;
    private Long startTime;
    private Long timeInUse;

    private LocalDateTime createDate;

    private String period;
}
