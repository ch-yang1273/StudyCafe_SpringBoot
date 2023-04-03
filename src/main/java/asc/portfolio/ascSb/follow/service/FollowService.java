package asc.portfolio.ascSb.follow.service;

import asc.portfolio.ascSb.follow.domain.FollowFinder;
import asc.portfolio.ascSb.follow.domain.FollowManager;
import asc.portfolio.ascSb.follow.dto.CafeFollowersResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class FollowService {

    private final FollowFinder followFinder;
    private final FollowManager followManager;

    @Transactional
    public void follow(Long userId, Long cafeId) {
        followManager.following(userId, cafeId);
    }

    @Transactional
    public void unfollow(Long userId, Long cafeId) {
        followManager.unfollowing(userId, cafeId);
    }

    @Transactional(readOnly = true)
    public CafeFollowersResponse getFollowers(Long cafeId) {
        Long count = followFinder.getFollowersCount(cafeId);
        return new CafeFollowersResponse(cafeId, count);
    }
}
