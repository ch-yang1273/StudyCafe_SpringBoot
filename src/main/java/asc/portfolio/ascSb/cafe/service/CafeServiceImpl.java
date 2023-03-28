package asc.portfolio.ascSb.cafe.service;

import asc.portfolio.ascSb.cafe.domain.Cafe;
import asc.portfolio.ascSb.cafe.domain.CafeFinder;
import asc.portfolio.ascSb.cafe.domain.CafeRepository;
import asc.portfolio.ascSb.cafe.dto.CafeRegistrationRequest;
import asc.portfolio.ascSb.cafe.dto.CafeResponse;
import asc.portfolio.ascSb.user.domain.User;
import asc.portfolio.ascSb.user.domain.UserFinder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CafeServiceImpl implements CafeService {

    private final CafeRepository cafeRepository;
    private final CafeFinder cafeFinder;
    private final UserFinder userFinder;

    @Transactional(readOnly = true)
    @Override
    public Page<CafeResponse> getCafeList(Pageable pageable) {
        return cafeRepository.findAll(pageable)
                .map(cafe -> new CafeResponse(cafe.getId(), cafe.getCafeName(), cafe.getCafeArea()));
    }

    @Transactional(readOnly = true)
    @Override
    public CafeResponse findByAdminId(Long adminId) {
        Cafe cafe = cafeFinder.findByAdminId(adminId);
        return CafeResponse.of(cafe);
    }

    @Transactional
    @Override
    public void registerCafe(Long adminId, CafeRegistrationRequest dto) {
        User admin = userFinder.findById(adminId);
        Cafe newCafe = Cafe.createCafeWithAuth(admin, dto.getName(), dto.getArea());
        cafeRepository.save(newCafe);
    }

    @Transactional
    @Override
    public void openCafe(Long adminId, Long cafeId) {
        User admin = userFinder.findById(adminId);
        Cafe cafe = cafeFinder.findById(cafeId);
        cafe.openCafe(admin);
    }

    @Transactional
    @Override
    public void closeCafe(Long adminId, Long cafeId) {
        User admin = userFinder.findById(adminId);
        Cafe cafe = cafeFinder.findById(cafeId);
        cafe.closeCafe(admin);
    }
}
