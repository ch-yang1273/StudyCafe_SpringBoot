package asc.portfolio.ascSb.common.infra.fcm.dto.fcmtoken;

import asc.portfolio.ascSb.adminfcmtoken.domain.AdminFCMToken;
import asc.portfolio.ascSb.cafe.domain.Cafe;
import asc.portfolio.ascSb.user.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminFCMTokenRequestDto {

    private User user;
    private Cafe cafe;
    private String fCMToken;

    @Builder
    public AdminFCMTokenRequestDto(User user, Cafe cafe, String fCMToken) {
        this.user = user;
        this.cafe = cafe;
        this.fCMToken = fCMToken;
    }

    public AdminFCMToken toEntity() {
        return AdminFCMToken.builder()
                .user(user)
                .cafe(cafe)
                .fCMToken(fCMToken)
                .build();
    }
}
