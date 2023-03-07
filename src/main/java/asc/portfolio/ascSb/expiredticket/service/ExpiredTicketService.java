package asc.portfolio.ascSb.expiredticket.service;

import asc.portfolio.ascSb.ticket.domain.Ticket;

import java.util.List;

public interface ExpiredTicketService {

    boolean transferInvalidTicket(List<Ticket> ticketList);
}
