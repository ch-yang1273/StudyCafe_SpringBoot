package asc.portfolio.ascSb.user.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserTokenRequestDto {
    private String accessToken;
    private String refreshToken;
}
