package asc.portfolio.ascSb.reservation.domain;

import asc.portfolio.ascSb.cafe.domain.Cafe;
import asc.portfolio.ascSb.common.exception.exception.RuleViolationException;
import asc.portfolio.ascSb.reservation.infra.validator.CafeSeatReservationRule;
import asc.portfolio.ascSb.reservation.infra.validator.CafeTicketReservationRule;
import asc.portfolio.ascSb.reservation.infra.validator.SeatReservationRule;
import asc.portfolio.ascSb.seat.domain.Seat;
import asc.portfolio.ascSb.support.User.UserFixture;
import asc.portfolio.ascSb.support.cafe.CafeFixture;
import asc.portfolio.ascSb.support.seat.SeatFixture;
import asc.portfolio.ascSb.support.ticket.TicketFixture;
import asc.portfolio.ascSb.ticket.domain.Ticket;
import asc.portfolio.ascSb.user.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

@Slf4j
class ReservationValidatorTest {

    public static Stream<Arguments> validateDataProvider() {
        List<Arguments> list = new ArrayList<>();

        User admin = UserFixture.ADMIN_BUTTERCUP.toUser();
        User user = UserFixture.DAISY.toUser();

        Cafe openCafe = CafeFixture.CAFE_A.toCafe(admin);
        openCafe.openCafe(admin);
        Cafe closedCafe = CafeFixture.CAFE_B.toCafe(admin);

        Seat seatOfOpenCafe = SeatFixture.SEAT_A.toSeat(openCafe.getId());
        Seat seatOfClosedCafe = SeatFixture.SEAT_B.toSeat(closedCafe.getId());
        Seat seatStatusReserved = SeatFixture.SEAT_C.toSeat(openCafe.getId());
        seatStatusReserved.startSeatUsage(null);

        Ticket ticketOfOpenCafe = TicketFixture.TICKET_A.toFixedTermTicket(user, openCafe, LocalDate.now().plusDays(1));
        Ticket ticketOfClosedCafe = TicketFixture.TICKET_A.toFixedTermTicket(user, closedCafe, LocalDate.now().plusDays(1));

        Ticket usableTicket = TicketFixture.TICKET_B.toFixedTermTicket(user, openCafe, LocalDate.now().plusDays(1));
        Ticket unusableTicket = TicketFixture.TICKET_D.toFixedTermTicket(user, openCafe, LocalDate.now().plusDays(1));


        // 테스트 시나리오
        String scenario;
        scenario = "1. 모든 Validation 통과";
        list.add(Arguments.of(scenario, user, openCafe, seatOfOpenCafe, usableTicket, 0));
        scenario = "2. 해당하는 카페에 속하지 않은 좌석은 Validation 실패";
        list.add(Arguments.of(scenario, user, openCafe, seatOfClosedCafe, ticketOfOpenCafe, 1));
        scenario = "3. 닫힌 카페는 Validation 실패";
        list.add(Arguments.of(scenario, user, closedCafe, seatOfClosedCafe, ticketOfClosedCafe, 1));
        scenario = "4. 사용 만료된 티켓으로는 Validation 실패";
        list.add(Arguments.of(scenario, user, openCafe, seatOfOpenCafe, unusableTicket, 1));
        scenario = "5. 해당하는 카페의 티켓이 아니면 Validation 실패";
        list.add(Arguments.of(scenario, user, openCafe, seatOfOpenCafe, ticketOfClosedCafe, 1));
        scenario = "6. 사용 중인 좌석은 Validation 실패";
        list.add(Arguments.of(scenario, user, openCafe, seatStatusReserved, ticketOfOpenCafe, 1));

        return list.stream();
    }

    @ParameterizedTest
    @MethodSource("validateDataProvider")
    @DisplayName("CafeSeatReservationRule, CafeTicketReservationRule 통합 테스트")
    void validate(String message, User user, Cafe cafe, Seat seat, Ticket ticket, int expect) {
        log.info("Running Test: {}", message);

        // given
        List<ReservationRule> Rules = new ArrayList<>();
        Rules.add(new CafeSeatReservationRule());
        Rules.add(new CafeTicketReservationRule());
        Rules.add(new SeatReservationRule());
        ReservationValidator validator = new ReservationValidator(Rules);

        // When
        RuleViolationException exception = null;
        if (expect == 0) {
            validator.validate(user, cafe, seat, ticket);
        } else {
            Throwable throwable = catchThrowable(() -> validator.validate(user, cafe, seat, ticket));
            assertThat(throwable).isInstanceOf(RuleViolationException.class);
            exception = (RuleViolationException) throwable;
        }

        // Then
        if (expect == 0) {
            assertThat(exception).isNull();
        } else {
            assertThat(exception.getResponses().size()).isEqualTo(expect);
        }
    }
}