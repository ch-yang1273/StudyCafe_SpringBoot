package asc.portfolio.ascSb.reservation.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class ReleaseReservationRequest {
    Long cafeId;
    Long seatId;
}
