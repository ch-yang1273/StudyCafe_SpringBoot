package asc.portfolio.ascSb.user.dto;

import asc.portfolio.ascSb.user.domain.User;
import asc.portfolio.ascSb.user.domain.UserRoleType;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class UserProfile {
    private final Long id;
    private final String loginId;
    private final String email;
    private final UserRoleType role;
    private final String name;
    private final LocalDateTime createDate;

    public UserProfile(User user) {
        this.id = user.getId();
        this.loginId = user.getLoginId();
        this.email = user.getEmail();
        this.role = user.getRole();
        this.name = user.getName();
        this.createDate = user.getCreateDate();
    }
}
