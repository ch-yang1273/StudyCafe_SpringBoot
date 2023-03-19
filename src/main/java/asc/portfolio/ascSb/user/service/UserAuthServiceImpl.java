package asc.portfolio.ascSb.user.service;

import asc.portfolio.ascSb.common.dto.TokenPayload;
import asc.portfolio.ascSb.user.domain.PasswordEncoder;
import asc.portfolio.ascSb.user.domain.TokenRepository;
import asc.portfolio.ascSb.user.domain.TokenService;
import asc.portfolio.ascSb.user.domain.User;
import asc.portfolio.ascSb.user.domain.UserFinder;
import asc.portfolio.ascSb.user.domain.UserRepository;
import asc.portfolio.ascSb.user.domain.UserRoleType;
import asc.portfolio.ascSb.user.dto.UserLoginResponseDto;
import asc.portfolio.ascSb.user.dto.UserSignupDto;
import asc.portfolio.ascSb.user.exception.UserErrorData;
import asc.portfolio.ascSb.user.exception.UserException;
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
        User targetUser = userFinder.findByLoginId(loginId);
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
        User findUser = userFinder.findById(userId);
        return new UserLoginResponseDto(findUser.getRole(), tokenService.createAccessToken(payload), refreshToken);
    }

    //todo : 추후 security 적용 후 메서드 삭제
    private void checkRole(Long userId, UserRoleType roleType) {
        User user = userFinder.findById(userId);

        if (user.getRole() != roleType) {
            switch (user.getRole()) {
                case USER:
                    throw new UserException(UserErrorData.USER_NEED_USER_ROLE);
                case ADMIN:
                    throw new UserException(UserErrorData.USER_NEED_ADMIN_ROLE);
                default:
                    throw new IllegalStateException("알 수 없는 유저 권한");
            }
        }
    }

    @Transactional(readOnly = true)
    @Override
    public void checkAdminRole(Long adminId) {
        checkRole(adminId, UserRoleType.ADMIN);
    }

    @Transactional(readOnly = true)
    @Override
    public void checkUserRole(Long userId) {
        checkRole(userId, UserRoleType.USER);
    }
}
