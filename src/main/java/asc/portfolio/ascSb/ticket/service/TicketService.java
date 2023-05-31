package asc.portfolio.ascSb.ticket.service;

import asc.portfolio.ascSb.cafe.domain.Cafe;
import asc.portfolio.ascSb.cafe.domain.CafeFinder;
import asc.portfolio.ascSb.common.domain.CurrentTimeProvider;
import asc.portfolio.ascSb.ticket.domain.Ticket;
import asc.portfolio.ascSb.ticket.domain.TicketFinder;
import asc.portfolio.ascSb.ticket.domain.TicketRepository;
import asc.portfolio.ascSb.ticket.dto.TicketCreationInfo;
import asc.portfolio.ascSb.user.domain.User;
import asc.portfolio.ascSb.user.domain.UserFinder;
import asc.portfolio.ascSb.ticket.dto.TicketStatusResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class TicketService {

    private final TicketRepository ticketRepository;
    private final TicketFinder ticketFinder;
    private final UserFinder userFinder;
    private final CafeFinder cafeFinder;

    private final CurrentTimeProvider currentTimeProvider;

    @Transactional(readOnly = true)
    public List<TicketStatusResponse> getAllUserTickets(Long userId) {
        List<Ticket> tickets = ticketFinder.findAllByUserId(userId);
        return tickets.stream()
                .map(TicketStatusResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public TicketStatusResponse userValidTicket(Long userId, String cafeName) {
        Cafe cafe = cafeFinder.findByCafeName(cafeName);

        Ticket ticket = ticketFinder.findTicketByUserIdAndCafeIdAndInUseStatus(userId, cafe.getId());
        return TicketStatusResponse.of(ticket);
    }

    @Transactional
    public void createTicket(TicketCreationInfo creationInfo) {
        Ticket ticket = creationInfo.toEntity(currentTimeProvider.localDateNow());
        ticketRepository.save(ticket);
    }

    @Transactional
    public List<TicketStatusResponse> lookupUserTickets(String userLoginId, Long adminId) {
        User user = userFinder.findByLoginId(userLoginId);
        Cafe cafe = cafeFinder.findByAdminId(adminId);

        return ticketFinder.findAllByUserIdAndCafeId(user.getId(), cafe.getId())
                .stream()
                .map(TicketStatusResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional
    public void setInvalidTicket(Long orderId) {
        Ticket ticket = ticketFinder.findByOrderId(orderId);
        ticket.changeTicketStateToInvalid();
    }
}
