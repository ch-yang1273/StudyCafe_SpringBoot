package asc.portfolio.ascSb.reservation.infra.validator;

import asc.portfolio.ascSb.cafe.domain.Cafe;
import asc.portfolio.ascSb.common.exception.dto.ValidationResponse;
import asc.portfolio.ascSb.reservation.dto.ValidationContext;
import asc.portfolio.ascSb.seat.domain.Seat;
import asc.portfolio.ascSb.support.User.UserFixture;
import asc.portfolio.ascSb.support.cafe.CafeFixture;
import asc.portfolio.ascSb.support.seat.SeatFixture;
import asc.portfolio.ascSb.user.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
class CafeSeatReservationRuleTest {

    private void printValidationResponses(List<ValidationResponse> responses) {
        for (ValidationResponse response : responses) {
            log.info("field = {}", response.getField());
            log.info("message = {}", response.getMessage());
        }
    }

    public static Stream<Arguments> ruleDataProvider() {
        List<Arguments> list = new ArrayList<>();

        User admin = UserFixture.ADMIN_BUTTERCUP.toUser();

        Cafe openCafe = CafeFixture.CAFE_A.toCafe(admin);
        openCafe.openCafe(admin);
        Cafe closedCafe = CafeFixture.CAFE_B.toCafe(admin);

        Seat seat1 = SeatFixture.SEAT_A.toSeat(openCafe.getId());
        Seat seat2 = SeatFixture.SEAT_B.toSeat(closedCafe.getId());

        Seat inUseSeat = SeatFixture.SEAT_C.toSeat(openCafe.getId());
        inUseSeat.startSeatUsage(null);

        // 1. 열린 Cafe, 사용 중이 아닌 Seat, 0
        list.add(Arguments.of(openCafe, seat1, 0));

        // 2. 닫힌 Cafe, 사용 중이 아닌 Seat, 0
        list.add(Arguments.of(closedCafe, seat2, 1));

        // 3. 열린 Cafe, 사용 중인 Seat, 1
        list.add(Arguments.of(openCafe, inUseSeat, 1));

        // 4. 닫힌 카페, 사용 중인 좌석, +매핑 되지 않는 카페&좌석, 3
        list.add(Arguments.of(closedCafe, inUseSeat, 3));

        return list.stream();
    }

    @ParameterizedTest
    @MethodSource("ruleDataProvider")
    @DisplayName("CafeSeatReservationRule 조건 테스트")
    void CafeSeatReservationRule(Cafe cafe, Seat seat, int expect) {
        //given
        CafeSeatReservationRule sut = new CafeSeatReservationRule();
        ValidationContext context = ValidationContext.builder()
                .cafe(cafe)
                .seat(seat)
                .build();

        //when
        List<ValidationResponse> list = sut.validate(context);
        printValidationResponses(list);

        //then
        assertThat(list.size()).isEqualTo(expect);
    }
}