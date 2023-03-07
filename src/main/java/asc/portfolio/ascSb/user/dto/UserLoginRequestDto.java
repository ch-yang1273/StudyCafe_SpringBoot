package asc.portfolio.ascSb.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
public class UserLoginRequestDto {

  @Schema(description = "아이디", example = "tUser_A_login")
  @NotBlank
  @Length(min = 8, max = 16)
  private String loginId;

  @Schema(description = "패스워드", example = "tUser_A_password")
  @NotBlank
  @Length(min = 8)
  private String password;

  @Builder
  public UserLoginRequestDto(String loginId, String password) {
    this.loginId = loginId;
    this.password = password;
  }
}
