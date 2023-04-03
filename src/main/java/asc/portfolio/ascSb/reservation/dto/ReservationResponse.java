package asc.portfolio.ascSb.reservation.dto;

import asc.portfolio.ascSb.reservation.domain.Reservation;
import asc.portfolio.ascSb.reservation.domain.ReservationStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
public class ReservationResponse {
    private Long Id;
    private Long seatId;
    private ReservationStatus status;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public static ReservationResponse of(Reservation reservation) {
        return ReservationResponse.builder()
                .id(reservation.getId())
                .seatId(reservation.getSeatId())
                .status(reservation.getStatus())
                .startTime(reservation.getStartTime())
                .endTime(reservation.getEndTime())
                .build();
    }

    @Builder
    public ReservationResponse(Long id, Long seatId, ReservationStatus status, LocalDateTime startTime, LocalDateTime endTime) {
        Id = id;
        this.seatId = seatId;
        this.status = status;
        this.startTime = startTime;
        this.endTime = endTime;
    }
}
