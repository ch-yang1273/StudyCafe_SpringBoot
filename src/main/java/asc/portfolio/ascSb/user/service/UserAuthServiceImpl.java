package asc.portfolio.ascSb.user.service;

import asc.portfolio.ascSb.common.dto.TokenPayload;
import asc.portfolio.ascSb.user.domain.*;
import asc.portfolio.ascSb.user.dto.*;
import asc.portfolio.ascSb.user.exception.AccessDeniedException;
import asc.portfolio.ascSb.user.exception.UnknownUserException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserAuthServiceImpl implements UserAuthService {

    private final UserRepository userRepository;

    private final TokenRepository tokenRepository;

    private final PasswordEncoder passwordEncoder;

    private final TokenService tokenService;

    @Transactional
    @Override
    public void signUp(UserSignupDto signUpDto) {
        User newUser = signUpDto.toEntity();
        newUser.encryptPassword(passwordEncoder);

        userRepository.save(newUser);
    }

    private UserLoginResponseDto createTokenResponse(User user) {
        String accessToken = tokenService.createAccessToken(new TokenPayload(user.getId()));
        String refreshToken = tokenService.createRefreshToken();

        return UserLoginResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .roleType(user.getRole())
                .build();
    }

    @Transactional
    @Override
    public UserLoginResponseDto checkPassword(String loginId, String password) {
        User targetUser = userRepository.findByLoginId(loginId).orElseThrow(() -> new UnknownUserException());
        targetUser.checkPassword(passwordEncoder, loginId, password);

        UserLoginResponseDto result = createTokenResponse(targetUser);

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
    public UserLoginResponseDto reissueToken(String accessToken, String refreshToken) {
        // AccessToken 에서 Payload 추출 - 만료 검증 없이
        TokenPayload payload = tokenService.noVerifyAndGetPayload(accessToken);
        Long userId = payload.getUserId();

        tokenService.verifyAndGetPayload(refreshToken, tokenRepository.getToken(userId.toString()));

        // AccessToken 과 RefreshToken 재발급, refreshToken 저장
        User findUser = userRepository.findById(userId).orElseThrow(() -> new UnknownUserException());
        return new UserLoginResponseDto(findUser.getRole(), tokenService.createAccessToken(payload), refreshToken);
    }

    //todo : 추후 security 적용 후 메서드 삭제
    @Transactional(readOnly = true)
    @Override
    public void checkAdminRole(Long userId) {
        User findUser = userRepository.findById(userId).orElseThrow();
        if (findUser.getRole() != UserRoleType.ADMIN) {
            throw new AccessDeniedException("need admin role");
        }
    }
}
