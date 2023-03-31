package asc.portfolio.ascSb.user.service;

import asc.portfolio.ascSb.common.domain.CurrentTimeProvider;
import asc.portfolio.ascSb.common.dto.TokenPayload;
import asc.portfolio.ascSb.user.domain.PasswordEncoder;
import asc.portfolio.ascSb.user.domain.TokenRepository;
import asc.portfolio.ascSb.user.domain.TokenService;
import asc.portfolio.ascSb.user.domain.User;
import asc.portfolio.ascSb.user.domain.UserFinder;
import asc.portfolio.ascSb.user.domain.UserRepository;
import asc.portfolio.ascSb.user.domain.TokenPairDto;
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

    private final CurrentTimeProvider currentTimeProvider;

    @Transactional
    @Override
    public void signUp(UserSignupRequest signUpDto) {
        User newUser = signUpDto.toEntity();
        newUser.encryptPassword(passwordEncoder);

        userRepository.save(newUser);
    }

    @Transactional
    @Override
    public TokenPairDto checkPassword(String loginId, String password) {
        User user = userFinder.findByLoginId(loginId);
        user.checkPassword(passwordEncoder, loginId, password);

        TokenPairDto tokenPair = tokenService.createTokenPair(loginId, password, currentTimeProvider.dateNow());

        tokenRepository.saveToken(user.getId().toString(), tokenPair.getRefreshToken(), tokenService.getRefreshTime());
        return tokenPair;
    }

    @Transactional(readOnly = true)
    @Override
    public Long checkAccessToken(String token) {
        TokenPayload payload = tokenService.verifyAccessToken(token);
        return payload.getUserId();
    }

    @Transactional(readOnly = true)
    @Override
    public TokenPairDto reissueToken(String accessToken, String refreshToken) {
        TokenPayload payload = tokenService.verifyRefreshToken(accessToken, refreshToken);
        String reissuedAccessToken = tokenService.createAccessToken(payload, currentTimeProvider.dateNow());

        User user = userFinder.findById(payload.getUserId());
        return new TokenPairDto(user.getRole(), reissuedAccessToken, refreshToken);
    }
}
