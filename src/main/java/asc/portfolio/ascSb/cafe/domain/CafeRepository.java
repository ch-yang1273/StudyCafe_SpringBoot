package asc.portfolio.ascSb.cafe.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CafeRepository extends JpaRepository<Cafe, Long> {

    Optional<Cafe> findByCafeName(String cafeName);

    Optional<Cafe> findByAdminId(Long id);

    Cafe findByCafeNameContains(String cafeName); //todo 삭제
}
