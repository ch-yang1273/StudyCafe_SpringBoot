package asc.portfolio.ascSb.follow.controller;

import asc.portfolio.ascSb.follow.dto.CafeFollowersResponse;
import asc.portfolio.ascSb.common.auth.LoginUser;
import asc.portfolio.ascSb.follow.service.FollowService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/follow")
public class FollowController {

    private final FollowService followService;

    @PostMapping("/{cafeId}/following")
    public ResponseEntity<Void> followCafe(@LoginUser Long userId, @PathVariable Long cafeId) {
        followService.follow(userId, cafeId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{cafeId}/unfollowing")
    public ResponseEntity<Void> unfollowCafe(@LoginUser Long userId, @PathVariable Long cafeId) {
        log.debug("followService = {}", followService);
        followService.unfollow(userId, cafeId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{cafeId}/followers")
    public ResponseEntity<CafeFollowersResponse> followers(@PathVariable Long cafeId) {
        return ResponseEntity.ok().body(followService.getFollowers(cafeId));
    }
}
