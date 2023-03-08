package asc.portfolio.ascSb.common.auth.jwt;

import asc.portfolio.ascSb.common.infra.redis.RedisRepository;
import asc.portfolio.ascSb.user.domain.User;
import asc.portfolio.ascSb.user.dto.UserLoginResponseDto;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtService {

    private final JwtTokenProvider jwtTokenProvider;

    private final RedisRepository redisRepository;

    public UserLoginResponseDto createTokenWithLogin(User user) {

        if (user == null) {
            return null;
        }

        return createAllToken(user);
    }

    public String createAccessToken(String loginId) {
        return jwtTokenProvider.createAccessToken(loginId);
    }

    // 외부 호출 제한.
    private String createRefreshToken() {
        return jwtTokenProvider.createRefreshToken();
    }

    public UserLoginResponseDto createAllToken(User user) {
        // token 생성
        String accessToken = createAccessToken(user.getLoginId());
        String refreshToken = createRefreshToken();

        log.info("accessToken={}", accessToken);
        log.info("refreshToken={}", refreshToken);

        // refreshToken 저장 (redis)
        redisRepository.saveValue(user.getLoginId(), refreshToken, jwtTokenProvider.getRefreshTime());

        return new UserLoginResponseDto(user.getRole(), accessToken, refreshToken);
    }

    public Claims validCheckAndGetBody(String token) {
        return jwtTokenProvider.validCheckAndGetBody(token);
    }

    public Claims noValidCheckAndGetBody(String token) {
        return jwtTokenProvider.noValidCheckAndGetBody(token);
    }

    public String validCheckAndGetSubject(String token) {
        return jwtTokenProvider.validCheckAndGetSubject(token);
    }

    public String noValidCheckAndGetSubject(String token) {
        return jwtTokenProvider.noValidCheckAndGetSubject(token);
    }
}
