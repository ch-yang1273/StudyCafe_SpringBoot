package asc.portfolio.ascSb.common.auth.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Slf4j
@Component
public class JwtTokenProvider {

    //  private final String secretKey;
    private final Key secretKey;
    private final long expireTime;
    private final long refreshTime;

    public long getExpireTime() {
        return expireTime;
    }

    public long getRefreshTime() {
        return refreshTime;
    }

    public JwtTokenProvider(@Value("${jwt.secret}") String secretKey,
                            @Value("${jwt.expiration-in-seconds}") Long expireTime,
                            @Value("${jwt.refresh-in-hour}") Long refreshTime) {

        final long second = 1_000L;
        final long minute = 60 * second;
        final long hour = 60 * minute;

        this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
        this.expireTime = expireTime * second;
        this.refreshTime = refreshTime * hour;
    }

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

    public String createRefreshToken() {
        Date now = new Date();
        Date expireDate = new Date(now.getTime() + refreshTime);

        return Jwts.builder()
                .setIssuedAt(now)
                .setExpiration(expireDate)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public Claims validCheckAndGetBody(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            log.debug("만료된 JWT 토큰입니다.");
            throw new ExpiredJwtException(e.getHeader(), e.getClaims(), "만료된 JWT 토큰입니다.");
        } catch (JwtException e) {
            log.debug("올바르지 않은 JWT 토큰입니다.");
            throw new JwtException("올바르지 않은 JWT 토큰입니다.");
        }
    }

    public Claims noValidCheckAndGetBody(String token) {
        try {
            return this.validCheckAndGetBody(token);
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    public String validCheckAndGetSubject(String token) {
        return validCheckAndGetBody(token)
                .getSubject();
    }

    public String noValidCheckAndGetSubject(String token) {
        return noValidCheckAndGetBody(token)
                .getSubject();
    }
}