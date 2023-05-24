package asc.portfolio.ascSb.ticket.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface TicketRepository extends JpaRepository<Ticket, Long>, TicketCustomRepository {

    Optional<Ticket> findByOrderId(Long orderId);

    List<Ticket> findAllByUserIdAndCafeId(Long userId, Long cafeId);

    List<Ticket> findAllByUserId(Long userId);
}
