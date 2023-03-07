package asc.portfolio.ascSb.user.dto;

import asc.portfolio.ascSb.user.domain.User;
import asc.portfolio.ascSb.user.domain.UserRoleType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
public class UserSignupDto {

  @Schema(description = "아이디", example = "testUserId")
  @NotBlank(message = "로그인 아이디는 필수 입력 값입니다.")
  @Length(min = 8, max = 16)
  private String loginId;

  @Schema(description = "패스워드", example = "abcdef123456")
  @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
  @Length(min = 8)
  private String password;

  @Schema(description = "이메일", example = "ascProject@gmail.com")
  @NotBlank(message = "이메일은 필수 입력 값입니다.")
  @Email(message = "이메일 형식으로 입력해주세요.")
  private String email;

  @Schema(description = "이름", example = "testUserName")
  private String name;

  @Builder
  public UserSignupDto(String loginId, String password, String email, String name) {
    this.loginId = loginId;
    this.password = password;
    this.email = email;
    this.name = name;
  }

  public User toEntity() {
    return User.builder()
            .loginId(loginId)
            .password(password)
            .email(email)
            .name(name)
            .role(UserRoleType.USER)
            .build();
  }
}
