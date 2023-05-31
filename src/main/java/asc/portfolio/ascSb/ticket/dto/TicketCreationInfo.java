package asc.portfolio.ascSb.ticket.dto;

import asc.portfolio.ascSb.ticket.domain.Ticket;
import asc.portfolio.ascSb.ticket.domain.TicketStatus;
import asc.portfolio.ascSb.ticket.domain.TicketType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class TicketCreationInfo {

    private final Long userId;
    private final Long cafeId;
    private final Integer price;

    private final String typeString;

    private final int days; // fixed-term ticket
    private final Long totalDuration; // part-time ticket

    private final String productLabel;

    @Builder
    public TicketCreationInfo(Long userId, Long cafeId, Integer price, String typeString,
                              int days, Long totalDuration, String productLabel) {
        this.userId = userId;
        this.cafeId = cafeId;
        this.price = price;
        this.typeString = typeString;
        this.days = days;
        this.totalDuration = totalDuration;
        this.productLabel = productLabel;
    }

    public Ticket toEntity(LocalDate date) {
        return Ticket.builder()
                .userId(userId)
                .cafeId(cafeId)
                .status(TicketStatus.IN_USE)
                .price(price)
                .ticketType(TicketType.of(typeString))
                .expiryDate(date.plusDays(days))
                .totalDuration(totalDuration)
                .remainMinute(totalDuration)
                .productLabel(productLabel)
                .build();
    }
}
