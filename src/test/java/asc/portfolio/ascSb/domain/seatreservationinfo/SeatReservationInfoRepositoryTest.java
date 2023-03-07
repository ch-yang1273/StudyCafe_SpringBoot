package asc.portfolio.ascSb.domain.seatreservationinfo;


import asc.portfolio.ascSb.cafe.domain.CafeRepository;
import asc.portfolio.ascSb.seat.domain.SeatRepository;
import asc.portfolio.ascSb.ticket.domain.TicketRepository;
import asc.portfolio.ascSb.user.domain.UserRepository;
import asc.portfolio.ascSb.seatreservationinfo.domain.SeatReservationInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@Commit
public class SeatReservationInfoRepositoryTest {

    @Autowired
    SeatReservationInfoRepository seatReservationInfoRepository;

    @Autowired
    CafeRepository cafeRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    TicketRepository ticketRepository;

    @Autowired
    SeatRepository seatRepository;

}
