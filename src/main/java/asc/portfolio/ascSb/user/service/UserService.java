package asc.portfolio.ascSb.user.service;

import asc.portfolio.ascSb.user.domain.User;
import asc.portfolio.ascSb.user.dto.*;

public interface UserService {

  Long signUp(UserSignupDto signUpDto) throws Exception;

  UserLoginResponseDto checkPassword(String loginId, String password);

  User checkAccessToken(String jwt);

  UserLoginResponseDto reissueToken(UserTokenRequestDto tokenRequestDto);

  UserQrAndNameResponseDto userQrAndName(Long id);

  UserForAdminResponseDto AdminCheckUserInfo(String userId);

  boolean checkLoginId(String userLoginId);
}
