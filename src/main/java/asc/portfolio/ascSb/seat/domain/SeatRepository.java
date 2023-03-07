package asc.portfolio.ascSb.seat.domain;
import asc.portfolio.ascSb.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SeatRepository extends JpaRepository<Seat, Long>, SeatCustomRepository {

  Optional<Seat> findByUser(User user);

}