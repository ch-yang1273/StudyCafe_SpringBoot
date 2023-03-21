package asc.portfolio.ascSb.seat.dto;
import asc.portfolio.ascSb.seat.domain.Seat;
import lombok.Getter;

@Getter
public class SeatListResponseDto {

    private Long id;
    private int seatNumber;
    private boolean isReserved;
    private Long userId;
    private Long ticketId;

    public SeatListResponseDto(Seat seat) {
        this.id = seat.getId();
        this.seatNumber = seat.getSeatNumber();
        this.isReserved = seat.isReserved();
        this.userId = seat.getUserId();
        this.ticketId = seat.getTicket().getId();
    }
}
