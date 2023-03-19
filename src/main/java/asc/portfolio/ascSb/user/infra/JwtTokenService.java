package asc.portfolio.ascSb.user.infra;

import asc.portfolio.ascSb.common.domain.CurrentTimeProvider;
import asc.portfolio.ascSb.common.dto.TokenPayload;
import asc.portfolio.ascSb.user.domain.TokenService;
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

    private final CurrentTimeProvider currentTimeProvider;

    //  private final String secretKey;
    private final Key secretKey;
    private final long expireTime;
    private final long refreshTime;

    @Autowired
    public JwtTokenService(CurrentTimeProvider currentTimeProvider,
                           @Value("${jwt.secret}") String secretKey,
                           @Value("${jwt.expiration-in-seconds}") Long expireTime,
                           @Value("${jwt.refresh-in-hour}") Long refreshTime) {

        this.currentTimeProvider = currentTimeProvider;

        final long second = 1_000L;
        final long minute = 60 * second;
        final long hour = 60 * minute;

        this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
        this.expireTime = expireTime * second;
        this.refreshTime = refreshTime * hour;
    }

    @Override
    public long getExpireTime() {
        return expireTime;
    }

    @Override
    public long getRefreshTime() {
        return refreshTime;
    }

    @Override
    public String createAccessToken(TokenPayload payload) {
        Date now = currentTimeProvider.toDate(currentTimeProvider.now());
        Date expireDate = new Date(now.getTime() + expireTime);

        return Jwts.builder()
                .addClaims(payload.toMap())
                .setIssuedAt(now)
                .setExpiration(expireDate)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    @Override
    public String createRefreshToken() {
        Date now = currentTimeProvider.toDate(currentTimeProvider.now());
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
            throw new ExpiredTokenException(createTokenPayload(e.getClaims()),UserErrorData.USER_EXPIRED_TOKEN);
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
    public TokenPayload verifyAndGetPayload(String token) {
        String baseToken = checkFormat(token);
        return createTokenPayload(validCheckAndGetBody(baseToken));
    }

    @Override
    public TokenPayload verifyAndGetPayload(String token, String compare) {
        if (!token.equals(compare)) {
            throw new TokenException(UserErrorData.USER_WRONG_TOKEN);
        }
        String baseToken = checkFormat(token);
        return createTokenPayload(validCheckAndGetBody(baseToken));
    }

    @Override
    public TokenPayload noVerifyAndGetPayload(String token) {
        String baseToken = checkFormat(token);
        try {
            return createTokenPayload(validCheckAndGetBody(baseToken));
        } catch (ExpiredTokenException e) {
            return e.getPayload();
        }
    }
}