package asc.portfolio.ascSb.follow.domain;

import asc.portfolio.ascSb.cafe.domain.Cafe;
import asc.portfolio.ascSb.cafe.domain.CafeFinder;
import asc.portfolio.ascSb.follow.exception.FollowErrorData;
import asc.portfolio.ascSb.follow.exception.FollowException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class FollowFinder {

    private final FollowingRepository followingRepository;
    private final CafeFinder cafeFinder;

    public Cafe findFollowedCafe(Long userId) {
        Follow follow = followingRepository.findById(userId).orElseThrow(() -> new FollowException(FollowErrorData.NO_FOLLOWED_CAFE));
        return cafeFinder.findById(follow.getCafeId());
    }

    public Long getFollowersCount(Long cafeId) {
        return followingRepository.countByCafeId(cafeId);
    }
}
