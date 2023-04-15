package asc.portfolio.ascSb.user.service;

import asc.portfolio.ascSb.user.domain.TokenPairDto;
import asc.portfolio.ascSb.user.dto.UserSignupRequest;

public interface UserAuthService {

    void signup(UserSignupRequest signupDto);

    TokenPairDto checkPassword(String loginId, String password);

    Long checkAccessToken(String token);

    TokenPairDto reissueToken(String accessToken, String refreshToken);
}
