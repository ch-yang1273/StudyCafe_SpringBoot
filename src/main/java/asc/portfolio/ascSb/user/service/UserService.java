package asc.portfolio.ascSb.user.service;

import asc.portfolio.ascSb.user.domain.User;
import asc.portfolio.ascSb.user.dto.*;
import org.springframework.transaction.annotation.Transactional;

public interface UserService {

    void signUp(UserSignupDto signUpDto);

    UserLoginResponseDto checkPassword(String loginId, String password);

    User checkAccessToken(String token);

    UserLoginResponseDto reissueToken(String accessToken, String refreshToken);

    UserQrAndNameResponseDto userQrAndName(Long id);

    UserForAdminResponseDto AdminCheckUserInfo(String userId);

    void checkLoginId(String userLoginId);
}
