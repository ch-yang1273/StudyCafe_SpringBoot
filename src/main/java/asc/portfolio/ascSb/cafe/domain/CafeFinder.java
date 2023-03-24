package asc.portfolio.ascSb.cafe.domain;

import asc.portfolio.ascSb.cafe.exception.CafeErrorData;
import asc.portfolio.ascSb.cafe.exception.CafeException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class CafeFinder {

    private final CafeRepository cafeRepository;
    private final FollowingRepository followingRepository;

    public Cafe findById(Long cafeId) {
        return cafeRepository.findById(cafeId).orElseThrow(() -> new CafeException(CafeErrorData.CAFE_NOT_FOUND));
    }

    public Cafe findByCafeName(String cafeName) {
        return cafeRepository.findByCafeName(cafeName).orElseThrow(() -> new CafeException(CafeErrorData.CAFE_NOT_FOUND));
    }

    public Cafe findByAdminId(Long adminId) {
        return cafeRepository.findByAdminId(adminId).orElseThrow(() -> new CafeException(CafeErrorData.CAFE_NOT_FOUND));
    }

    public Cafe findFollowedCafe(Long userId) {
        Following following = followingRepository.findById(userId).orElseThrow(() -> new CafeException(CafeErrorData.NO_FOLLOWED_CAFE));
        return cafeRepository.findById(following.getCafeId()).orElseThrow(() -> new CafeException(CafeErrorData.NO_FOLLOWED_CAFE));
    }
}
