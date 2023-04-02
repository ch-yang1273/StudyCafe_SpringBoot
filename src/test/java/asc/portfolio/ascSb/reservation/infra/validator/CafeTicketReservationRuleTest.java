package asc.portfolio.ascSb.reservation.infra.validator;

import asc.portfolio.ascSb.cafe.domain.Cafe;
import asc.portfolio.ascSb.common.exception.dto.ValidationResponse;
import asc.portfolio.ascSb.reservation.dto.ValidationContext;
import asc.portfolio.ascSb.support.User.UserFixture;
import asc.portfolio.ascSb.support.cafe.CafeFixture;
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

import static org.assertj.core.api.Assertions.*;

@Slf4j
class CafeTicketReservationRuleTest {

    private void printValidationResponses(List<ValidationResponse> responses) {
        for (ValidationResponse response : responses) {
            log.info("field = {}", response.getField());
            log.info("message = {}", response.getMessage());
        }
    }

    public static Stream<Arguments> ruleDataProvider() {
        List<Arguments> list = new ArrayList<>();

        User admin = UserFixture.ADMIN_BUTTERCUP.toUser();
        User user = UserFixture.DAISY.toUser();
        Cafe cafe1 = CafeFixture.CAFE_A.toCafe(admin);
        Cafe cafe2 = CafeFixture.CAFE_B.toCafe(admin);

        Ticket usableTicket = TicketFixture.TICKET_A.toFixedTermTicket(user, cafe1, LocalDate.now().plusDays(1));
        Ticket unusableTicket = TicketFixture.TICKET_D.toFixedTermTicket(user, cafe1, LocalDate.now().plusDays(1));

        // 1. Cafe1, usable ticket, 0
        list.add(Arguments.of(cafe1, usableTicket, 0));

        // 2. Cafe1, unusable ticket, 1
        list.add(Arguments.of(cafe1, unusableTicket, 1));

        // 3. Cafe2, usable ticket, 1
        list.add(Arguments.of(cafe2, usableTicket, 1));

        // 4. Cafe2, unusable ticket, 2
        list.add(Arguments.of(cafe2, unusableTicket, 2));

        return list.stream();
    }

    @ParameterizedTest
    @MethodSource("ruleDataProvider")
    @DisplayName("CafeTicketReservationRule 조건 테스트")
    void CafeTicketReservationRule(Cafe cafe, Ticket ticket, int expect) {
        //given
        CafeTicketReservationRule sut = new CafeTicketReservationRule();
        ValidationContext context = ValidationContext.builder()
                .cafe(cafe)
                .ticket(ticket)
                .build();

        //when
        List<ValidationResponse> list = sut.validate(context);
        printValidationResponses(list);

        //then
        assertThat(list.size()).isEqualTo(expect);
    }
}