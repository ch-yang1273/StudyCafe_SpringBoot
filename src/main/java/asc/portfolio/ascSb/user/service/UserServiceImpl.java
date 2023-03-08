package asc.portfolio.ascSb.user.service;

import asc.portfolio.ascSb.common.infra.redis.RedisRepository;
import asc.portfolio.ascSb.user.domain.PasswordEncoder;
import asc.portfolio.ascSb.user.domain.User;
import asc.portfolio.ascSb.user.domain.UserRepository;
import asc.portfolio.ascSb.common.auth.jwt.JwtService;
import asc.portfolio.ascSb.user.dto.*;
import asc.portfolio.ascSb.user.exception.UnknownUserException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final JwtService jwtService;

    private final RedisRepository redisRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public void signUp(UserSignupDto signUpDto) {
        User newUser = signUpDto.toEntity();
        newUser.encryptPassword(passwordEncoder);

        userRepository.save(newUser);
    }

    @Override
    public UserLoginResponseDto checkPassword(String loginId, String password) {
        User targetUser = userRepository.findByLoginId(loginId).orElseThrow(() -> new UnknownUserException());
        targetUser.checkPassword(passwordEncoder, loginId, password);

        return jwtService.createTokenWithLogin(targetUser);
    }

    @Override
    public User checkAccessToken(String jwt) {
        if ((jwt == null) || jwt.isBlank()) {
            throw new JwtException("jwt = null");
        }

        String[] jwtSplit = jwt.split(" ");
        if ((jwtSplit.length == 2) && (jwtSplit[0].equals("Bearer"))) {
            jwt = jwtSplit[1];
        }

        log.debug("jwt = {}", jwt);
        String loginId = jwtService.validCheckAndGetSubject(jwt);

        return userRepository.findByLoginId(loginId).orElseThrow();
    }

    private Boolean isValidRefreshToken(String refreshToken, String loginId) {
        // refresh token 만료 검증 - 실패 시 throw
        Claims claims = jwtService.validCheckAndGetBody(refreshToken);

        // loginId 로 redis 에서 refresh token 검색 및 비교
        String findToken = redisRepository.getValue(loginId);
        return findToken.equals(refreshToken);
    }

    public UserLoginResponseDto reissueToken(UserTokenRequestDto tokenRequestDto) {

        String accessToken = tokenRequestDto.getAccessToken();
        String refreshToken = tokenRequestDto.getRefreshToken();

        // AccessToken 에서 LoginId (subject) 추출 - 만료 검증 없이
        String loginId = jwtService.noValidCheckAndGetSubject(accessToken);

        // refresh token 검증
        log.debug("Retrieve the refresh token from the repository");
        if (!this.isValidRefreshToken(refreshToken, loginId)) {
            return null;
        }

        // AccessToken 과 RefreshToken 재발급, refreshToken 저장
        log.debug("Reissue access token");
        User findUser = userRepository.findByLoginId(loginId).orElseThrow();
        return new UserLoginResponseDto(findUser.getRole(), jwtService.createAccessToken(loginId), refreshToken);
    }

    @Override
    public UserQrAndNameResponseDto userQrAndName(Long id) {
        return userRepository.findQrAndUserNameById(id);
    }

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

    @Override
    public void checkLoginId(String userLoginId) {
        userRepository.findByLoginId(userLoginId).orElseThrow(() -> new UnknownUserException());
    }
}
