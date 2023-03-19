package asc.portfolio.ascSb.user.domain;

import asc.portfolio.ascSb.user.exception.UserErrorData;
import asc.portfolio.ascSb.user.exception.UserException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class UserFinder {

    private final UserRepository userRepository;

    public User findById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new UserException(UserErrorData.USER_NOT_FOUND));
    }

    public User findByLoginId(String loginId) {
        return userRepository.findByLoginId(loginId).orElseThrow(() -> new UserException(UserErrorData.USER_NOT_FOUND));
    }
}
