package asc.portfolio.ascSb.seat.dto;
import asc.portfolio.ascSb.seat.domain.Seat;
import asc.portfolio.ascSb.ticket.domain.Ticket;
import asc.portfolio.ascSb.user.domain.User;
import lombok.Getter;

@Getter
public class SeatListResponseDto {

    private Long id;
    private int seatNumber;
    private boolean isReserved;
    private User user;
    private Ticket ticket;

    public SeatListResponseDto(Seat seat) {
        this.id = seat.getId();
        this.seatNumber = seat.getSeatNumber();
        this.isReserved = seat.isReserved();
        this.user = seat.getUser();
        this.ticket = seat.getTicket();
    }
}
