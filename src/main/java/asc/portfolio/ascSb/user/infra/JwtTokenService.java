package asc.portfolio.ascSb.user.infra;

import asc.portfolio.ascSb.user.domain.TokenService;
import asc.portfolio.ascSb.user.exception.ExpiredTokenException;
import asc.portfolio.ascSb.user.exception.TokenException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Slf4j
@Component
public class JwtTokenService implements TokenService {

    //  private final String secretKey;
    private final Key secretKey;
    private final long expireTime;
    private final long refreshTime;

    @Autowired
    public JwtTokenService(@Value("${jwt.secret}") String secretKey,
                           @Value("${jwt.expiration-in-seconds}") Long expireTime,
                           @Value("${jwt.refresh-in-hour}") Long refreshTime) {

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
    public String createAccessToken(String subject) {
        Date now = new Date();
        Date expireDate = new Date(now.getTime() + expireTime);

        return Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(now)
                .setExpiration(expireDate)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    @Override
    public String createRefreshToken() {
        Date now = new Date();
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
            throw new ExpiredTokenException(e.getClaims().getSubject(), "만료된 JWT 토큰입니다.");
        } catch (JwtException e) {
            log.debug("올바르지 않은 JWT 토큰입니다.");
            throw new TokenException("올바르지 않은 JWT 토큰입니다.");
        }
    }

    @Override
    public String validCheckAndGetSubject(String token) {
        return validCheckAndGetBody(token)
                .getSubject();
    }

    @Override
    public String noValidCheckAndGetSubject(String token) {
        try {
            return this.validCheckAndGetBody(token)
                    .getSubject();
        } catch (ExpiredTokenException e) {
            return e.getSubject();
        }
    }
}