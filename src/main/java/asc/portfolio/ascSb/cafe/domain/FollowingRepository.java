package asc.portfolio.ascSb.cafe.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowingRepository extends JpaRepository<Following, Long> {

    Long countByCafeId(Long cafeId);
}
