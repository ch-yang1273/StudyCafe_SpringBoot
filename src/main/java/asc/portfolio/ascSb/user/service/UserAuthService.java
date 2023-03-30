package asc.portfolio.ascSb.user.service;

import asc.portfolio.ascSb.user.dto.UserLoginResponse;
import asc.portfolio.ascSb.user.dto.UserSignupRequest;

public interface UserAuthService {

    void signUp(UserSignupRequest signUpDto);

    UserLoginResponse checkPassword(String loginId, String password);

    Long checkAccessToken(String token);

    UserLoginResponse reissueToken(String accessToken, String refreshToken);
}
