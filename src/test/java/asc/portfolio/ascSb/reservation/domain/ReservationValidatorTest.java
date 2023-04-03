package asc.portfolio.ascSb.reservation.domain;

import asc.portfolio.ascSb.cafe.domain.Cafe;
import asc.portfolio.ascSb.common.exception.dto.ValidationResponse;
import asc.portfolio.ascSb.common.exception.exception.RuleViolationException;
import asc.portfolio.ascSb.reservation.infra.validator.CafeSeatReservationRule;
import asc.portfolio.ascSb.reservation.infra.validator.CafeTicketReservationRule;
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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;

@Slf4j
class ReservationValidatorTest {

    private void printValidationResponses(List<ValidationResponse> responses) {
        for (ValidationResponse response : responses) {
            log.info("field = {}", response.getField());
            log.info("message = {}", response.getMessage());
        }
    }

    public static Stream<Arguments> validateDataProvider() {
        List<Arguments> list = new ArrayList<>();

        User admin = UserFixture.ADMIN_BUTTERCUP.toUser();
        User user = UserFixture.DAISY.toUser();

        Cafe openCafe = CafeFixture.CAFE_A.toCafe(admin);
        openCafe.openCafe(admin);
        Cafe closedCafe = CafeFixture.CAFE_B.toCafe(admin);

        Seat seatOfOpenCafe = SeatFixture.SEAT_A.toSeat(openCafe.getId());
        Seat seatOfClosedCafe = SeatFixture.SEAT_B.toSeat(closedCafe.getId());

        Ticket ticketOfOpenCafe = TicketFixture.TICKET_A.toFixedTermTicket(user, openCafe, LocalDate.now().plusDays(1));
        Ticket ticketOfClosedCafe = TicketFixture.TICKET_A.toFixedTermTicket(user, closedCafe, LocalDate.now().plusDays(1));

        Ticket usableTicket = TicketFixture.TICKET_B.toFixedTermTicket(user, openCafe, LocalDate.now().plusDays(1));
        Ticket unusableTicket = TicketFixture.TICKET_D.toFixedTermTicket(user, openCafe, LocalDate.now().plusDays(1));

        Reservation reservation = new Reservation(user, openCafe, seatOfOpenCafe, usableTicket, LocalDateTime.now()); // Add Reservation instance creation code

        // 테스트 시나리오
        String scenario;
        scenario = "1. 모든 Validation 통과";
        list.add(Arguments.of(scenario, reservation, user, openCafe, seatOfOpenCafe, usableTicket, 0));
        scenario = "2. 해당하는 카페에 속하지 않은 좌석은 예약 불가";
        list.add(Arguments.of(scenario, reservation, user, openCafe, seatOfClosedCafe, ticketOfOpenCafe, 1));
        scenario = "3. 닫힌 카페는 예약 불가";
        list.add(Arguments.of(scenario, reservation, user, closedCafe, seatOfClosedCafe, ticketOfClosedCafe, 1));
        scenario = "4. 사용 만료된 티켓으로는 예약 실패";
        list.add(Arguments.of(scenario, reservation, user, openCafe, seatOfOpenCafe, unusableTicket, 1));
        scenario = "5. 해당하는 카페의 티켓이 아니면 예약 실패";
        list.add(Arguments.of(scenario, reservation, user, openCafe, seatOfOpenCafe, ticketOfClosedCafe, 1));

        return list.stream();
    }

    @ParameterizedTest
    @MethodSource("validateDataProvider")
    @DisplayName("CafeSeatReservationRule, CafeTicketReservationRule 통합 테스트")
    void validate(String message, Reservation reservation, User user, Cafe cafe, Seat seat, Ticket ticket, int expect) {
        log.info("Running Test: {}", message);

        List<ReservationRule> Rules = new ArrayList<>();
        Rules.add(new CafeSeatReservationRule());
        Rules.add(new CafeTicketReservationRule());
        ReservationValidator validator = new ReservationValidator(Rules);

        // When
        RuleViolationException exception = null;
        try {
            validator.validate(reservation, user, cafe, seat, ticket);
        } catch (RuleViolationException e) {
            exception = e;
            printValidationResponses(e.getResponses());
        }

        // Then
        if (expect == 0) {
            assertThat(exception).isNull();
        } else {
            assertThat(exception.getResponses().size()).isEqualTo(expect);
        }
    }
}