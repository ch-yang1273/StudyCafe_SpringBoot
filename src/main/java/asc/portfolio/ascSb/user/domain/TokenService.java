package asc.portfolio.ascSb.user.domain;

public interface TokenService {
    long getExpireTime();

    long getRefreshTime();

    String createAccessToken(String subject);

    String createRefreshToken();

    String validCheckAndGetSubject(String token);

    String noValidCheckAndGetSubject(String token);
}
