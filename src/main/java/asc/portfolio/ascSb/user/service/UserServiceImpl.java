package asc.portfolio.ascSb.user.service;

import asc.portfolio.ascSb.user.domain.*;
import asc.portfolio.ascSb.user.dto.*;
import asc.portfolio.ascSb.user.exception.AccessDeniedException;
import asc.portfolio.ascSb.user.exception.UnknownUserException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

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
        String accessToken = tokenService.createAccessToken(user.getId().toString());
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
    public User checkAccessToken(String token) {
        String subject = tokenService.verifyAndGetSubject(token);
        long userId = Long.parseLong(subject);

        // todo : pk만 return 하도록 수정
        return userRepository.findById(userId).orElseThrow(() -> new UnknownUserException());
    }

    @Transactional(readOnly = true)
    @Override
    public UserLoginResponseDto reissueToken(String accessToken, String refreshToken) {
        // AccessToken 에서 LoginId (subject) 추출 - 만료 검증 없이
        String subject = tokenService.noVerifyAndGetSubject(accessToken);
        long userId = Long.parseLong(subject);

        tokenService.verifyAndGetSubject(refreshToken, tokenRepository.getToken(subject));

        // AccessToken 과 RefreshToken 재발급, refreshToken 저장
        User findUser = userRepository.findById(userId).orElseThrow(() -> new UnknownUserException());
        return new UserLoginResponseDto(findUser.getRole(), tokenService.createAccessToken(subject), refreshToken);
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

    @Transactional(readOnly = true)
    @Override
    public UserQrAndNameResponseDto userQrAndName(Long id) {

        User findUser = userRepository.findById(id).orElseThrow(() -> new UnknownUserException());
        return new UserQrAndNameResponseDto(findUser.getName(), findUser.getQrCode());
    }

    @Transactional(readOnly = true)
    @Override
    public UserProfileDto getUserInfo(String userLoginId) {
        User findUser = userRepository.findByLoginId(userLoginId).orElseThrow(() -> new UnknownUserException());
        return new UserProfileDto(findUser);
    }
}
