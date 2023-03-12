package asc.portfolio.ascSb.user.domain;

public interface TokenService {
    long getExpireTime();

    long getRefreshTime();

    String createAccessToken(String subject);

    String createRefreshToken();

    String verifyAndGetSubject(String token);

    String verifyAndGetSubject(String token, String compare);

    String noVerifyAndGetSubject(String token);
}
