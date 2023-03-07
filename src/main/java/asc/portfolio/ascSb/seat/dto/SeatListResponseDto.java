package asc.portfolio.ascSb.seat.dto;
import asc.portfolio.ascSb.seat.domain.Seat;
import asc.portfolio.ascSb.seat.domain.SeatStateType;
import asc.portfolio.ascSb.ticket.domain.Ticket;
import asc.portfolio.ascSb.user.domain.User;
import lombok.Getter;

@Getter
public class SeatListResponseDto {

    private Long id;
    private int seatNumber;
    private SeatStateType seatState;
    private User user;
    private Ticket ticket;

    public SeatListResponseDto(Seat entity) {
        this.id = entity.getId();
        this.seatNumber = entity.getSeatNumber();
        this.seatState = entity.getSeatState();
        this.user = entity.getUser();
        this.ticket = entity.getTicket();
    }
}
