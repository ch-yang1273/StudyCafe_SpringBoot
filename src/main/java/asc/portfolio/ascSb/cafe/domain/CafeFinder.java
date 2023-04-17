package asc.portfolio.ascSb.cafe.domain;

import asc.portfolio.ascSb.cafe.exception.CafeErrorData;
import asc.portfolio.ascSb.cafe.exception.CafeException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class CafeFinder {

    private final CafeRepository cafeRepository;

    public Cafe findById(Long cafeId) {
        return cafeRepository.findById(cafeId).orElseThrow(() -> new CafeException(CafeErrorData.CAFE_NOT_FOUND));
    }

    public Cafe findByCafeName(String cafeName) {
        return cafeRepository.findByCafeName(cafeName).orElseThrow(() -> new CafeException(CafeErrorData.CAFE_NOT_FOUND));
    }

    public Cafe findByAdminId(Long adminId) {
        return cafeRepository.findByAdminId(adminId).orElseThrow(() -> new CafeException(CafeErrorData.ADMIN_NOT_FOUND));
    }
}
