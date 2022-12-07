package asc.portfolio.ascSb.web.dto.user;

import asc.portfolio.ascSb.domain.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class UserForAdminResponseDto {
    private String loginId;
    private String email;
    private String name;
    private LocalDateTime createDate;

    public UserForAdminResponseDto(User user) {
        this.loginId = user.getLoginId();
        this.email = user.getEmail();
        this.name = user.getName();
        this.createDate = user.getCreateDate();
    }
}
