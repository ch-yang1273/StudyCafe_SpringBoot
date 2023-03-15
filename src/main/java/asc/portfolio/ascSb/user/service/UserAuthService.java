package asc.portfolio.ascSb.user.service;

import asc.portfolio.ascSb.user.domain.User;
import asc.portfolio.ascSb.user.dto.*;

public interface UserAuthService {

    void signUp(UserSignupDto signUpDto);

    UserLoginResponseDto checkPassword(String loginId, String password);

    User checkAccessToken(String token);

    UserLoginResponseDto reissueToken(String accessToken, String refreshToken);

    void checkAdminRole(Long userId);
}
