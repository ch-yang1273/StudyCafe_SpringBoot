package asc.portfolio.ascSb.seat.domain;
import asc.portfolio.ascSb.cafe.domain.Cafe;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SeatRepository extends JpaRepository<Seat, Long>, SeatCustomRepository {

  List<Seat> findByCafe(Cafe cafe);

}