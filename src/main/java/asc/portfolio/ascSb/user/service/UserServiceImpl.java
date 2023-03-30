package asc.portfolio.ascSb.user.service;

import asc.portfolio.ascSb.user.domain.User;
import asc.portfolio.ascSb.user.domain.UserFinder;
import asc.portfolio.ascSb.user.dto.UserProfile;
import asc.portfolio.ascSb.user.dto.UserQrCodeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserFinder userFinder;

    @Transactional(readOnly = true)
    @Override
    public UserQrCodeResponse userQrAndName(Long id) {
        User findUser = userFinder.findById(id);
        return new UserQrCodeResponse(findUser.getName(), findUser.getQrCode());
    }

    @Transactional(readOnly = true)
    @Override
    public UserProfile getProfileById(Long userId) {
        User user = userFinder.findById(userId);
        return new UserProfile(user);
    }

    @Transactional(readOnly = true)
    @Override
    public UserProfile getUserInfoByLoginId(String loginId) {
        User user = userFinder.findByLoginId(loginId);
        return new UserProfile(user);
    }
}
