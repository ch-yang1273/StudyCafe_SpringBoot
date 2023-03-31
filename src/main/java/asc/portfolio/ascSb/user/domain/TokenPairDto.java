package asc.portfolio.ascSb.user.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class TokenPairDto {

    UserRoleType roleType;
    String accessToken;
    String refreshToken;

    @Builder
    public TokenPairDto(UserRoleType roleType, String accessToken, String refreshToken) {
        this.roleType = roleType;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
