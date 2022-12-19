package asc.portfolio.ascSb.service.seat;

import asc.portfolio.ascSb.domain.cafe.Cafe;
import asc.portfolio.ascSb.domain.cafe.CafeRepository;
import asc.portfolio.ascSb.domain.seat.Seat;
import asc.portfolio.ascSb.domain.seat.SeatRepository;
import asc.portfolio.ascSb.domain.seat.SeatStateType;
import asc.portfolio.ascSb.domain.ticket.Ticket;
import asc.portfolio.ascSb.domain.ticket.TicketRepository;
import asc.portfolio.ascSb.domain.ticket.TicketStateType;
import asc.portfolio.ascSb.domain.user.User;
import asc.portfolio.ascSb.domain.user.UserRepository;
import asc.portfolio.ascSb.domain.user.UserRoleType;
import asc.portfolio.ascSb.web.dto.seat.SeatResponseDto;
import asc.portfolio.ascSb.web.dto.seat.SeatSelectResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.*;

@Slf4j
@SpringBootTest
@Transactional
class SeatServiceTest {

    @Autowired
    SeatRepository seatRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    TicketRepository ticketRepository;

    @Autowired
    CafeRepository cafeRepository;

    //Service
    @Autowired
    SeatService seatService;

    @DisplayName("해당 카페의 Seat State Dto 조회 시, 상태 검증 후 반환")
    @Test
    public void seatListCheck() {
        //given
        Cafe cafe = Cafe.builder()
                .cafeName("testCafe")
                .build();

        cafeRepository.save(cafe);

        for(int i=0; i < 40; i ++) {
            Seat seat = Seat.builder()
                    .seatNumber(i)
                    .cafe(cafe)
                    .build();
            seatRepository.save(seat);
        }

        String userString = "TestUser";

        User user = User.builder()
                .loginId(userString + "_login")
                .password(userString + "_password")
                .email(userString + "@gmail.com")
                .name(userString)
                .role(UserRoleType.USER)
                .build();

        user.changeCafe(cafe);
        userRepository.save(user);

        LocalDateTime date = LocalDateTime.now();
        Ticket ticket = Ticket.builder()
                .cafe(cafe)
                .user(user)
                .isValidTicket(TicketStateType.VALID)
                .ticketPrice(3000)
                .fixedTermTicket(date.plusSeconds(2))
                .partTimeTicket(null)
                .remainingTime(null)
                .build();
        ticketRepository.save(ticket);

        Boolean isReserved = seatService.reserveSeat(user, 5, 10L);
        //log.info("isReserved={}", isReserved);

        //when
        List<SeatSelectResponseDto> listA = seatService.showCurrentAllSeatState(cafe.getCafeName());

        try {
            TimeUnit.SECONDS.sleep(4);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        List<SeatSelectResponseDto> listB = seatService.showCurrentAllSeatState(cafe.getCafeName());

        //then
        int countA = 0;
        int countB = 0;

        for (SeatSelectResponseDto dto : listA) {
            if (dto.getSeatState() == SeatStateType.RESERVED) {
                countA++;
            }
        }

        for (SeatSelectResponseDto dto : listB) {
            if (dto.getSeatState() == SeatStateType.RESERVED) {
                countB++;
            }
        }

        assertThat(countA).isEqualTo(1);
        assertThat(countB).isEqualTo(0);
    }

    @DisplayName("하나의 Seat 상태 조회")
    @Test
    public void showSeatStateOne() {
        //given
        Cafe cafe = Cafe.builder()
                .cafeName("testCafe")
                .build();

        cafeRepository.save(cafe);

        for(int i=0; i < 40; i ++) {
            Seat seat = Seat.builder()
                    .seatNumber(i)
                    .cafe(cafe)
                    .build();
            seatRepository.save(seat);
        }

        String userString = "TestUser";

        User user = User.builder()
                .loginId(userString + "_login")
                .password(userString + "_password")
                .email(userString + "@gmail.com")
                .name(userString)
                .role(UserRoleType.USER)
                .build();

        user.changeCafe(cafe);
        userRepository.save(user);

        LocalDateTime date = LocalDateTime.now();
        Ticket ticket = Ticket.builder()
                .cafe(cafe)
                .user(user)
                .isValidTicket(TicketStateType.VALID)
                .ticketPrice(3000)
                .fixedTermTicket(date.plusHours(1))
                .partTimeTicket(null)
                .remainingTime(null)
                .build();
        ticketRepository.save(ticket);

        Boolean isReserved = seatService.reserveSeat(user, 5, 10L);
        //log.info("isReserved={}", isReserved);

        //when
        SeatResponseDto reservedSeat = seatService.showSeatStateOne(cafe.getCafeName(), 5);
        SeatResponseDto unreservedSeat = seatService.showSeatStateOne(cafe.getCafeName(), 3);

        //then
        assertThat(reservedSeat.getSeatState()).isEqualTo(SeatStateType.RESERVED);
        assertThat(unreservedSeat.getSeatState()).isEqualTo(SeatStateType.UNRESERVED);
    }
}