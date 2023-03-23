package asc.portfolio.ascSb.user.service;

import asc.portfolio.ascSb.cafe.domain.Cafe;
import asc.portfolio.ascSb.cafe.domain.CafeRepository;
import asc.portfolio.ascSb.cafe.exception.CafeErrorData;
import asc.portfolio.ascSb.cafe.exception.CafeException;
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
    public UserProfileDto getProfileById(Long userId) {
        User user = userFinder.findById(userId);
        return new UserProfileDto(user);
    }

    @Transactional(readOnly = true)
    @Override
    public UserProfileDto getUserInfoByLoginId(String loginId) {
        User user = userFinder.findByLoginId(loginId);
        return new UserProfileDto(user);
    }

    @Transactional
    @Override
    public void updateUserCafe(Long userId, Long cafeId) {
        User user = userFinder.findById(userId);
        //todo : 삭제. Following으로 이동
        Cafe cafe = cafeRepository.findById(cafeId).orElseThrow(() -> new CafeException(CafeErrorData.CAFE_NOT_FOUND));
        user.changeCafe(cafe);
    }
}
