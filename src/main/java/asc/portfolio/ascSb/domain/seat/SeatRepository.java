package asc.portfolio.ascSb.domain.seat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeatRepository extends JpaRepository<Seat, Long>, SeatCustomRepository {
}