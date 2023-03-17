package asc.portfolio.ascSb.domain.seat;
import asc.portfolio.ascSb.cafe.domain.Cafe;
import asc.portfolio.ascSb.cafe.domain.CafeRepository;
import asc.portfolio.ascSb.product.domain.ProductRepository;
import asc.portfolio.ascSb.seatreservationinfo.domain.SeatReservationInfoRepository;
import asc.portfolio.ascSb.ticket.domain.Ticket;
import asc.portfolio.ascSb.ticket.domain.TicketRepository;
import asc.portfolio.ascSb.ticket.domain.TicketStateType;
import asc.portfolio.ascSb.user.domain.User;
import asc.portfolio.ascSb.user.domain.UserRepository;
import asc.portfolio.ascSb.user.domain.UserRoleType;
import asc.portfolio.ascSb.cafe.service.CafeService;
import asc.portfolio.ascSb.seat.domain.Seat;
import asc.portfolio.ascSb.seat.domain.SeatRepository;
import asc.portfolio.ascSb.seat.service.SeatService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@SpringBootTest
@Transactional
public class SeatRepositoryTest {

    @Autowired
    SeatRepository seatRepository;

    @Autowired
    SeatReservationInfoRepository seatReservationInfoRepository;

    @Autowired
    SeatService seatService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    TicketRepository ticketRepository;

    @Autowired
    CafeRepository cafeRepository;

    @Autowired
    CafeService cafeService;

    @Autowired
    ProductRepository productRepository;

    @BeforeEach
    public void before() {
        productRepository.deleteAllInBatch();
        seatReservationInfoRepository.deleteAllInBatch();
        ticketRepository.deleteAllInBatch();
        seatRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
        cafeRepository.deleteAllInBatch();
    }

    private Cafe createNewCafe(String cafeName) {
        Cafe cafe = Cafe.builder()
                .cafeName(cafeName)
                .build();

        return cafeRepository.saveAndFlush(cafe);
    }

    private User createNewUser(String prefix) {
        String loginId = prefix + "1234";
        String password = loginId;
        String email = prefix + "@gmail.com";
        String name = prefix;

        User user = User.builder()
                .loginId(loginId)
                .password(password)
                .email(email)
                .name(name)
                .role(UserRoleType.USER)
                .build();

        return userRepository.saveAndFlush(user);
    }

    private Seat createNewSeat(Cafe cafe, int seatNumber) {
        Seat seat = Seat.builder()
                .seatNumber(seatNumber)
                .cafe(cafe)
                .build();

        return seatRepository.saveAndFlush(seat);
    }

    private Ticket createNewFixedTicket(Cafe cafe, User user, LocalDateTime endTime) {
        Ticket ticket = Ticket.builder()
                .cafe(cafe)
                .user(user)
                .isValidTicket(TicketStateType.VALID)
                .ticketPrice(3000)
                .productLabel("FIXED-TERM" + 12123)
                .fixedTermTicket(endTime)
                .partTimeTicket(null)
                .remainingTime(null)
                .build();

        return ticketRepository.saveAndFlush(ticket);
    }

    private Ticket createNewPartTimeTicket(Cafe cafe, User user, Long remainingTime) {
        Ticket ticket = Ticket.builder()
                .cafe(cafe)
                .user(user)
                .isValidTicket(TicketStateType.VALID)
                .ticketPrice(3000)
                .productLabel("PART-TIME" + 12123)
                .fixedTermTicket(null)
                .partTimeTicket(60L)
                .remainingTime(remainingTime)
                .build();

        return ticketRepository.saveAndFlush(ticket);
    }

    @Test
    public void Seat_좌석생성기() {
        Cafe cafe = createNewCafe("testData_서울");
        User user = createNewUser("ascUser");
        Seat seat = createNewSeat(cafe, 0);
        Ticket ticket = createNewFixedTicket(cafe, user, LocalDateTime.now().plusSeconds(5));

        cafeService.changeReservedUserCafe(user.getId(), "testData_서울");

        seatService.reserveSeat(user.getId(), 0, 4L);
        seatRepository.saveAndFlush(seat);
    }

    @Test
    public void Seat_Update_With_FixedTicket_0() {
        Cafe cafe = createNewCafe("testData_서울");
        User user = createNewUser("ascUser");
        Seat seat = createNewSeat(cafe, 0);
        cafeService.changeReservedUserCafe(user.getId(), "testData_서울");

        // 3 초 후 만료되는 FixedTicket 으로 좌석 예약
        Ticket ticket = createNewFixedTicket(cafe, user, LocalDateTime.now().plusSeconds(3));

        seatService.reserveSeat(user.getId(), 0, 4L);

        // 4초 후 만료 여부 확인
        sleepMillis(4000);
        int count = seatService.updateAllReservedSeatState();
        Assertions.assertThat(count).isEqualTo(1);
    }

    @Test
    public void Seat_Update_With_FixedTicket_1() {
        Cafe cafe = createNewCafe("testData_서울");
        User user = createNewUser("ascUser");
        Seat seat = createNewSeat(cafe, 0);
        cafeService.changeReservedUserCafe(user.getId(), "testData_서울");

        // 100 초 후 만료되는 FixedTicket 으로 좌석 예약
        Ticket ticket = createNewFixedTicket(cafe, user, LocalDateTime.now().plusSeconds(100));

        seatService.reserveSeat(user.getId(), 0, 4L);

        // 4초 후 만료 여부 확인
        sleepMillis(4000);
        int count = seatService.updateAllReservedSeatState();
        Assertions.assertThat(count).isEqualTo(0);
    }

    @Test
    public void Seat_Update_With_StartTime() {
        Cafe cafe = createNewCafe("testData_서울");
        User user = createNewUser("ascUser");
        Seat seat = createNewSeat(cafe, 0);

        cafeService.changeReservedUserCafe(user.getId(), "testData_서울");
        Ticket ticket = createNewFixedTicket(cafe, user, LocalDateTime.now().plusSeconds(100));

        // 0시간 동안 좌석 예약 (만료 O)
        seatService.reserveSeat(user.getId(), 0, 0L);
        int count = seatService.updateAllReservedSeatState();
        Assertions.assertThat(count).isEqualTo(1);

        // 1시간 동안 좌석 예약 (만료 X)
        seatService.reserveSeat(user.getId(), 0, 1L);
        count = seatService.updateAllReservedSeatState();
        Assertions.assertThat(count).isEqualTo(0);
    }

    @Test
    public void Seat_Update_With_PartTimeTicket_종료X() {
        Cafe cafe = createNewCafe("testData_서울");
        User user = createNewUser("ascUser");
        Seat seat = createNewSeat(cafe, 0);
        cafeService.changeReservedUserCafe(user.getId(), "testData_서울");

        // 60 분 후 만료되는 PartTimeTicket 으로 좌석 예약
        Ticket ticket = createNewPartTimeTicket(cafe, user, 60L);

        seatService.reserveSeat(user.getId(), 0, 4L);

        // 바로 만료 확인
        int count = seatService.updateAllReservedSeatState();
        Assertions.assertThat(count).isEqualTo(0);
    }

    @Disabled
    @DisplayName("스케쥴러테스트")
    @Test
    public void Seat_Update_With_PartTimeTicket_종료O() {
        Cafe cafe = createNewCafe("testData_서울");
        User user = createNewUser("ascUser");
        Seat seat = createNewSeat(cafe, 0);
        cafeService.changeReservedUserCafe(user.getId(), "testData_서울");

        // 1 분 후 만료되는 PartTimeTicket 으로 좌석 예약
        Ticket ticket = createNewPartTimeTicket(cafe, user, 1L);

        seatService.reserveSeat(user.getId(), 0, 4L);

        // 1 분 후 만료 확인
        sleepMillis(65 * 1000);
        int count = seatService.updateAllReservedSeatState();
        Assertions.assertThat(count).isEqualTo(1);
    }

    private void sleepMillis(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            throw new RuntimeException("InterruptedException", e);
        }
    }
}
