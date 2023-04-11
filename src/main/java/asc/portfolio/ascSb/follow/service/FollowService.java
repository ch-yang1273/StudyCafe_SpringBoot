package asc.portfolio.ascSb.follow.service;

import asc.portfolio.ascSb.follow.domain.Follow;
import asc.portfolio.ascSb.follow.domain.FollowFinder;
import asc.portfolio.ascSb.follow.domain.FollowingRepository;
import asc.portfolio.ascSb.follow.dto.CafeFollowersResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class FollowService {

    private final FollowingRepository followingRepository;
    private final FollowFinder followFinder;

    @Transactional
    public void follow(Long userId, Long cafeId) {
        Optional<Follow> followOpt = followFinder.findOptionalById(userId);
        if (followOpt.isEmpty()) {
            followingRepository.save(new Follow(userId, cafeId));
            return;
        }
        Follow follow = followOpt.get();
        follow.changeFollowCafe(cafeId);
    }

    @Transactional
    public void unfollow(Long userId, Long cafeId) {
        Optional<Follow> followOpt = followFinder.findOptionalById(userId);
        if (followOpt.isPresent()) {
            Follow follow = followOpt.get();
            follow.unfollow(cafeId);
        }
    }

    @Transactional(readOnly = true)
    public CafeFollowersResponse getFollowers(Long cafeId) {
        Long count = followFinder.getFollowersCount(cafeId);
        return new CafeFollowersResponse(cafeId, count);
    }
}
