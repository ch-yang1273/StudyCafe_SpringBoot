package asc.portfolio.ascSb.cafe.service;

import asc.portfolio.ascSb.cafe.domain.Following;
import asc.portfolio.ascSb.cafe.domain.FollowingRepository;
import asc.portfolio.ascSb.cafe.dto.CafeFollowersResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class FollowingService {

    private final FollowingRepository followingRepository;

    @Transactional
    public void follow(Long userId, Long cafeId) {
        Optional<Following> followingOptional = followingRepository.findById(userId);
        if (followingOptional.isEmpty()) {
            followingRepository.save(new Following(userId, cafeId));
            return;
        }
        Following following = followingOptional.get();
        following.changeFollowCafe(cafeId);
    }

    @Transactional
    public void unfollow(Long userId, Long cafeId) {
        followingRepository.findById(userId).ifPresent(following -> following.unfollow(cafeId));
    }

    @Transactional(readOnly = true)
    public CafeFollowersResponse getFollowers(Long cafeId) {
        Long count = followingRepository.countByCafeId(cafeId);
        return new CafeFollowersResponse(cafeId, count);
    }
}
