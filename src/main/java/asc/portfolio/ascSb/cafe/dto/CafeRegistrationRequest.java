package asc.portfolio.ascSb.cafe.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CafeRegistrationRequest {

    @NotBlank(message = "카페명은 필수 입력 값 입니다.")
    String name;
    String area;
}
