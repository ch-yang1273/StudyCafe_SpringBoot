package asc.portfolio.ascSb.user.service;

import asc.portfolio.ascSb.common.infra.redis.RedisRepository;
import asc.portfolio.ascSb.user.domain.PasswordEncoder;
import asc.portfolio.ascSb.user.domain.TokenService;
import asc.portfolio.ascSb.user.domain.User;
import asc.portfolio.ascSb.user.domain.UserRepository;
import asc.portfolio.ascSb.user.dto.*;
import asc.portfolio.ascSb.user.exception.UnknownUserException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    //todo : 추상화
    private final RedisRepository redisRepository;

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
        String accessToken = tokenService.createAccessToken(user.getLoginId());
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

        redisRepository.saveValue(targetUser.getLoginId(), result.getRefreshToken(), tokenService.getRefreshTime());
        return result;
    }

    @Transactional(readOnly = true)
    @Override
    public User checkAccessToken(String token) {
        String loginId = tokenService.verifyAndGetSubject(token);
        // todo : pk만 return 하도록 수정
        return userRepository.findByLoginId(loginId).orElseThrow();
    }

    @Transactional(readOnly = true)
    @Override
    public UserLoginResponseDto reissueToken(String accessToken, String refreshToken) {
        // AccessToken 에서 LoginId (subject) 추출 - 만료 검증 없이
        String loginId = tokenService.noVerifyAndGetSubject(accessToken);

        tokenService.verifyAndGetSubject(refreshToken, redisRepository.getValue(loginId));

        // AccessToken 과 RefreshToken 재발급, refreshToken 저장
        User findUser = userRepository.findByLoginId(loginId).orElseThrow();
        return new UserLoginResponseDto(findUser.getRole(), tokenService.createAccessToken(loginId), refreshToken);
    }

    @Transactional(readOnly = true)
    @Override
    public UserQrAndNameResponseDto userQrAndName(Long id) {
        return userRepository.findQrAndUserNameById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public UserForAdminResponseDto AdminCheckUserInfo(String userLoginId) {
        Optional<User> user = userRepository.findByLoginId(userLoginId);
        try {
            if (user.isPresent()) {
                User userInfo = user.get();
                return new UserForAdminResponseDto(userInfo);
            }
        } catch (Exception e) {
            log.error("해당하는 유저가 없습니다");
        }
        return null;
    }

    @Transactional(readOnly = true)
    @Override
    public void checkLoginId(String userLoginId) {
        userRepository.findByLoginId(userLoginId).orElseThrow(() -> new UnknownUserException());
    }
}
