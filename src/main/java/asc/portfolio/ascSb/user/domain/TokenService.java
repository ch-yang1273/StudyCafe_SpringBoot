package asc.portfolio.ascSb.user.domain;

import asc.portfolio.ascSb.common.dto.TokenPayload;

import java.util.Date;

public interface TokenService {
    long getRefreshTime();

    TokenPairDto createTokenPair(String loginId, String password, Date now);

    String createAccessToken(TokenPayload payload, Date date);

    String createRefreshToken(Date now);

    TokenPayload verifyAccessToken(String token);

    TokenPayload verifyRefreshToken(String accessToken, String refreshToken);
}
