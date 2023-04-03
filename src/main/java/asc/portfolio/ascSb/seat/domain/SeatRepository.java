package asc.portfolio.ascSb.seat.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SeatRepository extends JpaRepository<Seat, Long>, SeatCustomRepository {

    List<Seat> findAllByCafeId(Long cafeId);
}