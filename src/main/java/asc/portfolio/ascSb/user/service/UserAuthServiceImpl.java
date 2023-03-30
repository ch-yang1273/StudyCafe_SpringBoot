package asc.portfolio.ascSb.user.service;

import asc.portfolio.ascSb.common.dto.TokenPayload;
import asc.portfolio.ascSb.user.domain.PasswordEncoder;
import asc.portfolio.ascSb.user.domain.TokenRepository;
import asc.portfolio.ascSb.user.domain.TokenService;
import asc.portfolio.ascSb.user.domain.User;
import asc.portfolio.ascSb.user.domain.UserFinder;
import asc.portfolio.ascSb.user.domain.UserRepository;
import asc.portfolio.ascSb.user.dto.UserLoginResponse;
import asc.portfolio.ascSb.user.dto.UserSignupRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserAuthServiceImpl implements UserAuthService {

    private final UserFinder userFinder;
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    @Transactional
    @Override
    public void signUp(UserSignupRequest signUpDto) {
        User newUser = signUpDto.toEntity();
        newUser.encryptPassword(passwordEncoder);

        userRepository.save(newUser);
    }

    private UserLoginResponse createTokenResponse(User user) {
        String accessToken = tokenService.createAccessToken(new TokenPayload(user.getId()));
        String refreshToken = tokenService.createRefreshToken();

        return UserLoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .roleType(user.getRole())
                .build();
    }

    @Transactional
    @Override
    public UserLoginResponse checkPassword(String loginId, String password) {
        User targetUser = userFinder.findByLoginId(loginId);
        targetUser.checkPassword(passwordEncoder, loginId, password);

        UserLoginResponse result = createTokenResponse(targetUser);

        tokenRepository.saveToken(targetUser.getId().toString(), result.getRefreshToken(), tokenService.getRefreshTime());
        return result;
    }

    @Transactional(readOnly = true)
    @Override
    public Long checkAccessToken(String token) {
        TokenPayload payload = tokenService.verifyAndGetPayload(token);
        return payload.getUserId();
    }

    @Transactional(readOnly = true)
    @Override
    public UserLoginResponse reissueToken(String accessToken, String refreshToken) {
        // AccessToken 에서 Payload 추출 - 만료 검증 없이
        TokenPayload payload = tokenService.noVerifyAndGetPayload(accessToken);
        Long userId = payload.getUserId();

        tokenService.verifyAndGetPayload(refreshToken, tokenRepository.getToken(userId.toString()));

        // AccessToken 과 RefreshToken 재발급, refreshToken 저장
        User findUser = userFinder.findById(userId);
        return new UserLoginResponse(findUser.getRole(), tokenService.createAccessToken(payload), refreshToken);
    }
}
