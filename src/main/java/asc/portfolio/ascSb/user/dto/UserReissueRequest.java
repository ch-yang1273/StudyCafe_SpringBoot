package asc.portfolio.ascSb.user.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserReissueRequest {
    private String accessToken;
    private String refreshToken;
}
