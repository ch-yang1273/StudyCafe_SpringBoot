package asc.portfolio.ascSb.ticket.domain;

import org.springframework.data.jpa.repository.JpaRepository;


public interface TicketRepository extends JpaRepository<Ticket, Long>, TicketCustomRepository {

    Ticket findByProductLabelContains(String productLabel);

}
