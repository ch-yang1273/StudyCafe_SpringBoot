package asc.portfolio.ascSb.follow.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowingRepository extends JpaRepository<Follow, Long> {

    Long countByCafeId(Long cafeId);
}
