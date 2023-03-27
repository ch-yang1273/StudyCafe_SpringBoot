package asc.portfolio.ascSb.reservation.dto;

import asc.portfolio.ascSb.reservation.domain.Reservation;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
public class CreateReservationRequest {

    Long cafeId;
    Long seatId;
    Long tickerId;

    public Reservation toEntity(Long userId, LocalDateTime now) {
        return Reservation.builder()
                .userId(userId)
                .cafeId(this.cafeId)
                .seatId(this.seatId)
                .ticketId(this.tickerId)
                .startTime(now)
                .build();
    }
}
