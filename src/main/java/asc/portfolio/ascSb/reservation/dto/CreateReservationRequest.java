package asc.portfolio.ascSb.reservation.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class CreateReservationRequest {

    Long cafeId;
    Long seatId;
    Long tickerId;
}
