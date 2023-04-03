package asc.portfolio.ascSb.user.service;

import asc.portfolio.ascSb.user.domain.TokenPairDto;
import asc.portfolio.ascSb.user.dto.UserSignupRequest;

public interface UserAuthService {

    void signUp(UserSignupRequest signUpDto);

    TokenPairDto checkPassword(String loginId, String password);

    Long checkAccessToken(String token);

    TokenPairDto reissueToken(String accessToken, String refreshToken);
}
