package asc.portfolio.ascSb.seat.domain;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SeatRepository extends JpaRepository<Seat, Long>, SeatCustomRepository {

  List<Seat> findByCafeId(Long cafeId);

  Optional<Seat> findByUserId(Long userId);

}