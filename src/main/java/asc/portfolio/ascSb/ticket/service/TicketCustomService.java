package asc.portfolio.ascSb.ticket.service;

import asc.portfolio.ascSb.commonenum.product.ProductNameType;

import java.time.LocalDateTime;

public interface TicketCustomService {

    default LocalDateTime distinguishFixedTermTicket(ProductNameType orderName) {
        switch (orderName) {
            case FOUR_WEEK_FIXED_TERM_TICKET:
                return LocalDateTime.now().plusDays(28);
            case THREE_WEEK_FIXED_TERM_TICKET:
                return LocalDateTime.now().plusDays(21);
            case TWO_WEEK_FIXED_TERM_TICKET:
                return LocalDateTime.now().plusDays(14);
            case WEEK_FIXED_TERM_TICKET:
                return LocalDateTime.now().plusDays(7);
            case TODAY_FIXED_TERM_TICKET:
                return LocalDateTime.now().plusDays(1);
            default: return LocalDateTime.now();
        }
    }

    default LocalDateTime distinguishUpdatedFixedTermTicket(ProductNameType orderName, LocalDateTime fixedTermTicket) {
        switch (orderName) {
            case FOUR_WEEK_FIXED_TERM_TICKET:
                return fixedTermTicket.plusDays(28);
            case THREE_WEEK_FIXED_TERM_TICKET:
                return fixedTermTicket.plusDays(21);
            case TWO_WEEK_FIXED_TERM_TICKET:
                return fixedTermTicket.plusDays(14);
            case WEEK_FIXED_TERM_TICKET:
                return fixedTermTicket.plusDays(7);
            case TODAY_FIXED_TERM_TICKET:
                return fixedTermTicket.plusDays(1);
            default: return fixedTermTicket;
        }
    }

    default Long distinguishPartTimeTicket(ProductNameType orderName) {
        final long multiply = 60L; //시단위 -> 분단위
        switch (orderName) {
            case HUNDRED_HOUR_PART_TIME_TICKET:
                return 100 * multiply;
            case FIFTY_HOUR_PART_TIME_TICKET:
                return 50 * multiply;
            case TEN_HOUR_PART_TIME_TICKET:
                return 10 * multiply;
            case FOUR_HOUR_PART_TIME_TICKET:
                return 4 * multiply;
            case ONE_HOUR_PART_TIME_TICKET:
                return 1 * multiply;
            default: return 0L;
        }
    }
}
