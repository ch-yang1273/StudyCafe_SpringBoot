package asc.portfolio.ascSb.follow.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@RequiredArgsConstructor
@Component
public class FollowManager {

    private final FollowingRepository followingRepository;

    public void following(Long userId, Long cafeId) {
        Optional<Follow> followingOptional = followingRepository.findById(userId);
        if (followingOptional.isEmpty()) {
            followingRepository.save(new Follow(userId, cafeId));
            return;
        }
        Follow follow = followingOptional.get();
        follow.changeFollowCafe(cafeId);
    }

    public void unfollowing(Long userId, Long cafeId) {
        followingRepository.findById(userId).ifPresent(following -> following.unfollow(cafeId));
    }
}
