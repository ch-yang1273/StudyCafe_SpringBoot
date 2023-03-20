package asc.portfolio.ascSb.support.User;

import asc.portfolio.ascSb.user.domain.User;
import asc.portfolio.ascSb.user.domain.UserRoleType;
import lombok.Getter;

@Getter
public enum UserFixture {

    BLOO("blooUser", "qwerasdf!@#", "bloo@gmail.com", UserRoleType.USER),
    COCO("cocoUser", "zxcvasdf!@#", "coco@gmail.com", UserRoleType.USER),
    DAISY("daisyUser", "poilkjhy!@#", "daisy@gmail.com", UserRoleType.USER),

    ADMIN_BLOSSOM("blossomAdmin", "qwertyui!@#", "blossom@admin.com", UserRoleType.ADMIN),
    ADMIN_BUTTERCUP("buttercupAdmin", "asdfghjk!@#", "buttercup@admin.com", UserRoleType.ADMIN)
    ;

    private final String loginId;
    private final String password;
    private final String email;
    private final UserRoleType roleType;

    UserFixture(String loginId, String password, String email, UserRoleType roleType) {
        this.loginId = loginId;
        this.password = password;
        this.email = email;
        this.roleType = roleType;
    }

    public User toUser() {
        return User.builder()
                .loginId(loginId)
                .password(password)
                .email(email)
                .role(roleType)
                .build();
    }
}
