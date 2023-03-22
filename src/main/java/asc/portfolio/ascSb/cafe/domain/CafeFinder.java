package asc.portfolio.ascSb.cafe.domain;

import asc.portfolio.ascSb.cafe.exception.CafeNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class CafeFinder {

    private final CafeRepository cafeRepository;

    public Cafe findById(Long cafeId) {
        // todo : CafeNotFoundException 삭제 해야함
        return cafeRepository.findById(cafeId).orElseThrow(() -> new CafeNotFoundException());
    }

    public Cafe findByCafeName(String cafeName) {
        return cafeRepository.findByCafeName(cafeName).orElseThrow(() -> new CafeNotFoundException());
    }
}
