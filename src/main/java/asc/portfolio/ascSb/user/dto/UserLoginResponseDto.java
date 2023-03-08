package asc.portfolio.ascSb.user.dto;

import asc.portfolio.ascSb.user.domain.UserRoleType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class UserLoginResponseDto {

    UserRoleType roleType;
    String accessToken;
    String refreshToken;

    @Builder
    public UserLoginResponseDto(UserRoleType roleType, String accessToken, String refreshToken) {
        this.roleType = roleType;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
