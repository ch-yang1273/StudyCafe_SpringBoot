package asc.portfolio.ascSb.user.service;

import asc.portfolio.ascSb.user.dto.UserLoginResponseDto;
import asc.portfolio.ascSb.user.dto.UserSignupDto;

public interface UserAuthService {

    void signUp(UserSignupDto signUpDto);

    UserLoginResponseDto checkPassword(String loginId, String password);

    Long checkAccessToken(String token);

    UserLoginResponseDto reissueToken(String accessToken, String refreshToken);
}
