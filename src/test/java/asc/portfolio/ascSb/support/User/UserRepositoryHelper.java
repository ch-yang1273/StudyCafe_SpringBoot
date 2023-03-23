package asc.portfolio.ascSb.support.User;

import asc.portfolio.ascSb.user.domain.PasswordEncoder;
import asc.portfolio.ascSb.user.domain.User;
import asc.portfolio.ascSb.user.domain.UserRepository;
import lombok.RequiredArgsConstructor;

@SuppressWarnings("NonAsciiCharacters")
@RequiredArgsConstructor
public class UserRepositoryHelper {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User 유저를_등록한다(UserFixture fixture) {
        User user = fixture.toUser();
        user.encryptPassword(passwordEncoder);
        return userRepository.save(user);
    }

    public User 유저를_찾는다(UserFixture fixture) {
        return userRepository.findByLoginId(fixture.getLoginId()).orElseThrow();
    }
}
