package asc.portfolio.ascSb.service.seat;

import asc.portfolio.ascSb.cafe.domain.Cafe;
import asc.portfolio.ascSb.cafe.domain.CafeRepository;
import asc.portfolio.ascSb.cafe.service.FollowingService;
import asc.portfolio.ascSb.seat.domain.Seat;
import asc.portfolio.ascSb.seat.domain.SeatRepository;
import asc.portfolio.ascSb.ticket.domain.Ticket;
import asc.portfolio.ascSb.ticket.domain.TicketRepository;
import asc.portfolio.ascSb.ticket.domain.TicketStateType;
import asc.portfolio.ascSb.user.domain.User;
import asc.portfolio.ascSb.user.domain.UserRepository;
import asc.portfolio.ascSb.user.domain.UserRoleType;
import asc.portfolio.ascSb.seat.service.SeatService;
import asc.portfolio.ascSb.seat.dto.SeatSelectResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
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

    @Autowired
    FollowingService followingService;

    //Service
    @Autowired
    SeatService seatService;

    //todo : seats 조회는 cafe 서비스로 이동
    @Disabled
    @DisplayName("스케쥴러 테스트. 해당 카페의 Seat State Dto 조회 시, 상태 검증 후 반환")
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
                    .cafeId(cafe.getId())
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

        followingService.follow(user.getId(), cafe.getId());
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

        Boolean isReserved = seatService.reserveSeat(user.getId(), 5, 10L);
        //log.info("isReserved={}", isReserved);

        //when
        List<SeatSelectResponseDto> listA = null; //seatService.getAllSeats(cafe.getId());

        try {
            TimeUnit.SECONDS.sleep(4);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        List<SeatSelectResponseDto> listB = null; //seatService.getAllSeats(cafe.getCafeName());

        //then
        int countA = 0;
        int countB = 0;

        for (SeatSelectResponseDto dto : listA) {
            if (dto.isReserved()) {
                countA++;
            }
        }

        for (SeatSelectResponseDto dto : listB) {
            if (dto.isReserved()) {
                countB++;
            }
        }

        assertThat(countA).isEqualTo(1);
        assertThat(countB).isEqualTo(0);
    }
}