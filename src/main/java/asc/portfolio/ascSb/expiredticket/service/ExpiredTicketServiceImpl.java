package asc.portfolio.ascSb.expiredticket.service;

import asc.portfolio.ascSb.expiredticket.domain.ExpiredTicket;
import asc.portfolio.ascSb.expiredticket.domain.ExpiredTicketRepository;
import asc.portfolio.ascSb.ticket.domain.Ticket;
import asc.portfolio.ascSb.expiredticket.dto.InvalidTicketToExpiredTicketDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ExpiredTicketServiceImpl implements ExpiredTicketService {

    private final ExpiredTicketRepository expiredTicketRepository;

    // invalid TicketList 를 ExpiredTicket Table에 insert
    @Override
    public boolean transferInvalidTicket(List<Ticket> ticket) {
        if(ticket.isEmpty()) {
            log.info("Invalid ticket does not exist");
            return false;
        }
        List<ExpiredTicket> expiredTickets = ticket.stream()
                .map(InvalidTicketToExpiredTicketDto::new)
                .map(InvalidTicketToExpiredTicketDto::toEntity)
                .collect(Collectors.toList());
        expiredTicketRepository.saveAll(expiredTickets);
        return true;
    }
}

