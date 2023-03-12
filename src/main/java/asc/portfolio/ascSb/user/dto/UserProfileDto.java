package asc.portfolio.ascSb.user.dto;

import asc.portfolio.ascSb.user.domain.User;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class UserProfileDto {
    private final String loginId;
    private final String email;
    private final String name;
    private final LocalDateTime createDate;

    public UserProfileDto(User user) {
        this.loginId = user.getLoginId();
        this.email = user.getEmail();
        this.name = user.getName();
        this.createDate = user.getCreateDate();
    }
}
