package asc.portfolio.ascSb.user.dto;

import asc.portfolio.ascSb.user.domain.UserRoleType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class UserLoginResponseDto {

  UserRoleType roleType;
  String accessToken;
  String refreshToken;
}
