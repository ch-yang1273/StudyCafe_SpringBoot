package asc.portfolio.ascSb.user.service;

import asc.portfolio.ascSb.cafe.domain.Cafe;
import asc.portfolio.ascSb.cafe.domain.CafeRepository;
import asc.portfolio.ascSb.cafe.exception.CafeNotFoundException;
import asc.portfolio.ascSb.user.domain.User;
import asc.portfolio.ascSb.user.domain.UserFinder;
import asc.portfolio.ascSb.user.dto.UserProfileDto;
import asc.portfolio.ascSb.user.dto.UserQrAndNameResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserFinder userFinder;
    private final CafeRepository cafeRepository;

    @Transactional(readOnly = true)
    @Override
    public UserQrAndNameResponseDto userQrAndName(Long id) {
        User findUser = userFinder.findById(id);
        return new UserQrAndNameResponseDto(findUser.getName(), findUser.getQrCode());
    }

    @Transactional(readOnly = true)
    @Override
    public UserProfileDto getUserInfoByLoginId(String loginId) {
        User findUser = userFinder.findByLoginId(loginId);
        return new UserProfileDto(findUser);
    }

    @Override
    public void updateUserCafe(Long userId, Long cafeId) {
        User user = userFinder.findById(userId);
        Cafe cafe = cafeRepository.findById(cafeId).orElseThrow(() -> new CafeNotFoundException());
        user.changeCafe(cafe);
    }
}
