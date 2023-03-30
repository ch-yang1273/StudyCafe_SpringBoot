package asc.portfolio.ascSb.support.User;

import asc.portfolio.ascSb.user.domain.User;
import asc.portfolio.ascSb.user.domain.UserRoleType;
import lombok.Getter;

@Getter
public enum UserFixture {

    BLOO(1L, "blooUser", "qwerasdf!@#", "bloo@gmail.com", UserRoleType.USER),
    COCO(2L,"cocoUser", "zxcvasdf!@#", "coco@gmail.com", UserRoleType.USER),
    DAISY(3L, "daisyUser", "poilkjhy!@#", "daisy@gmail.com", UserRoleType.USER),

    ADMIN_BLOSSOM(4L, "blossomAdmin", "qwertyui!@#", "blossom@admin.com", UserRoleType.ADMIN),
    ADMIN_BUTTERCUP(5L, "buttercupAdmin", "asdfghjk!@#", "buttercup@admin.com", UserRoleType.ADMIN)
    ;

    private final Long id;
    private final String loginId;
    private final String password;
    private final String email;
    private final UserRoleType roleType;

    UserFixture(Long id, String loginId, String password, String email, UserRoleType roleType) {
        this.id = id;
        this.loginId = loginId;
        this.password = password;
        this.email = email;
        this.roleType = roleType;
    }

    public User toUser() {
        return User.builder()
                .id(id)
                .loginId(loginId)
                .password(password)
                .email(email)
                .role(roleType)
                .build();
    }
}
