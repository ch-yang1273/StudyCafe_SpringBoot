package asc.portfolio.ascSb.seat.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SeatRepository extends JpaRepository<Seat, Long>, SeatCustomRepository {

    Optional<Seat> findByUserId(Long userId);

    List<Seat> findAllByCafeId(Long cafeId);
}