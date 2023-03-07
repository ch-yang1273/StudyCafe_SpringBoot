package asc.portfolio.ascSb.user.service;

import asc.portfolio.ascSb.user.domain.User;
import asc.portfolio.ascSb.user.dto.*;
import org.springframework.validation.BindingResult;

public interface UserService {

  void signUp(UserSignupDto signUpDto);

    String validateSingUp(BindingResult bindingResult);

    UserLoginResponseDto checkPassword(String loginId, String password);

  User checkAccessToken(String jwt);

  UserLoginResponseDto reissueToken(UserTokenRequestDto tokenRequestDto);

  UserQrAndNameResponseDto userQrAndName(Long id);

  UserForAdminResponseDto AdminCheckUserInfo(String userId);

  void checkLoginId(String userLoginId);
}
