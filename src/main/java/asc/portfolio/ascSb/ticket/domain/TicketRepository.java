package asc.portfolio.ascSb.ticket.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface TicketRepository extends JpaRepository<Ticket, Long>, TicketCustomRepository {

    Ticket findByProductLabelContains(String productLabel);

    List<Ticket> findAllByUserIdAndCafeId(Long userId, Long cafeId);

    List<Ticket> findAllByUserId(Long userId);
}
