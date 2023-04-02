package asc.portfolio.ascSb.seat.domain;

import asc.portfolio.ascSb.cafe.domain.Cafe;
import asc.portfolio.ascSb.cafe.domain.CafeRepository;
import asc.portfolio.ascSb.config.querydslconfig.TestQueryDslConfig;
import asc.portfolio.ascSb.support.User.UserFixture;
import asc.portfolio.ascSb.support.cafe.CafeFixture;
import asc.portfolio.ascSb.support.seat.SeatFixture;
import asc.portfolio.ascSb.user.domain.User;
import asc.portfolio.ascSb.user.domain.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.time.LocalDateTime;
import java.util.List;

@Import(TestQueryDslConfig.class)
@DataJpaTest
class SeatRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    CafeRepository cafeRepository;

    @Autowired
    SeatRepository seatRepository;

    @Test
    @DisplayName("사용 만료 된 Seats를 검색")
    public void findSeatsByStatusWithEndTimeAfter() {

        //given
        User user = UserFixture.ADMIN_BLOSSOM.toUser();
        Cafe cafe = CafeFixture.CAFE_A.toCafe(user);
        List<Seat> seats = SeatFixture.getSeats(cafe.getId());

        LocalDateTime now = LocalDateTime.now();
        int count = 0;
        for (Seat seat : seats) {
            count++;
            seat.startSeatUsage(SeatFixture.makeUsageData(user.getId(), now.plusMinutes(count)));
        }

        userRepository.save(user);
        cafeRepository.save(cafe);
        seatRepository.saveAll(seats);

        //when
        List<Seat> findSeats1 = seatRepository.findSeatsByStatusWithEndTimeBefore(SeatUsageStatus.IN_USE, now);
        List<Seat> findSeats2 = seatRepository.findSeatsByStatusWithEndTimeBefore(
                SeatUsageStatus.IN_USE, now.plusMinutes(2).plusSeconds(30));

        //then
        Assertions.assertThat(findSeats1.size()).isEqualTo(0);
        Assertions.assertThat(findSeats2.size()).isEqualTo(2);
    }
}