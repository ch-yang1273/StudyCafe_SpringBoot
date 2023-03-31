package asc.portfolio.ascSb.user.infra;

import asc.portfolio.ascSb.common.dto.TokenPayload;
import asc.portfolio.ascSb.user.domain.TokenPairDto;
import asc.portfolio.ascSb.user.domain.TokenRepository;
import asc.portfolio.ascSb.user.domain.TokenService;
import asc.portfolio.ascSb.user.domain.User;
import asc.portfolio.ascSb.user.domain.UserFinder;
import asc.portfolio.ascSb.user.exception.ExpiredTokenException;
import asc.portfolio.ascSb.user.exception.TokenException;
import asc.portfolio.ascSb.user.exception.UserErrorData;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtTokenService implements TokenService {

    private final UserFinder userFinder;
    private final TokenRepository tokenRepository;

    //  private final String secretKey;
    private final Key secretKey;
    private final long expireTime;
    private final long refreshTime;

    @Autowired
    public JwtTokenService(
            UserFinder userFinder,
            TokenRepository tokenRepository,
            @Value("${jwt.secret}") String secretKey,
            @Value("${jwt.expiration-in-seconds}") Long expireTime,
            @Value("${jwt.refresh-in-hour}") Long refreshTime) {

        this.userFinder = userFinder;
        this.tokenRepository = tokenRepository;

        final long second = 1_000L;
        final long minute = 60 * second;
        final long hour = 60 * minute;

        this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
        this.expireTime = expireTime * second;
        this.refreshTime = refreshTime * hour;
    }

    @Override
    public long getRefreshTime() {
        return refreshTime;
    }

    @Override
    public TokenPairDto createTokenPair(String loginId, String password, Date now) {
        User user = userFinder.findByLoginId(loginId);

        String accessToken = createAccessToken(new TokenPayload(user.getId()), now);
        String refreshToken = createRefreshToken(now);

        return new TokenPairDto(user.getRole(), accessToken, refreshToken);
    }

    @Override
    public String createAccessToken(TokenPayload payload, Date now) {
        Date expireDate = new Date(now.getTime() + expireTime);

        return Jwts.builder()
                .addClaims(payload.toMap())
                .setIssuedAt(now)
                .setExpiration(expireDate)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    @Override
    public String createRefreshToken(Date now) {
        Date expireDate = new Date(now.getTime() + refreshTime);

        return Jwts.builder()
                .setIssuedAt(now)
                .setExpiration(expireDate)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    private Claims validCheckAndGetBody(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            log.debug("만료된 JWT 토큰입니다.");
            throw new ExpiredTokenException(createTokenPayload(e.getClaims()), UserErrorData.USER_EXPIRED_TOKEN);
        } catch (JwtException e) {
            throw new TokenException(UserErrorData.USER_WRONG_TOKEN);
        }
    }

    private String checkFormat(String token) {
        log.debug("token = {}", token);
        if ((token == null) || token.isBlank()) {
            throw new TokenException(UserErrorData.USER_NO_TOKEN);
        }

        String[] tokenSplit = token.split(" ");
        if ((tokenSplit.length == 2) && (tokenSplit[0].equals("Bearer"))) {
            token = tokenSplit[1];
        }

        return token;
    }

    private TokenPayload createTokenPayload(Claims claims) {
        Long userId = claims.get("id", Long.class);
        return new TokenPayload(userId);
    }

    @Override
    public TokenPayload verifyAccessToken(String token) {
        String baseToken = checkFormat(token);
        return createTokenPayload(validCheckAndGetBody(baseToken));
    }

    @Override
    public TokenPayload verifyRefreshToken(String accessToken, String refreshToken) {
        TokenPayload payload = noVerifyAndGetPayload(accessToken);
        String token = tokenRepository.getToken(payload.getUserId().toString());

        if (!refreshToken.equals(token)) {
            throw new TokenException(UserErrorData.USER_WRONG_TOKEN);
        }
        return payload;
    }

    private TokenPayload noVerifyAndGetPayload(String token) {
        String baseToken = checkFormat(token);
        try {
            return createTokenPayload(validCheckAndGetBody(baseToken));
        } catch (ExpiredTokenException e) {
            return e.getPayload();
        }
    }
}