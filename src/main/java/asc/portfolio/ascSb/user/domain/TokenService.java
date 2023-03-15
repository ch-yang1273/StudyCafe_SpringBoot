package asc.portfolio.ascSb.user.domain;

import asc.portfolio.ascSb.common.dto.TokenPayload;

public interface TokenService {
    long getExpireTime();

    long getRefreshTime();

    String createAccessToken(TokenPayload payload);

    String createRefreshToken();

    TokenPayload verifyAndGetPayload(String token);

    TokenPayload verifyAndGetPayload(String token, String compare);

    TokenPayload noVerifyAndGetPayload(String token);
}
