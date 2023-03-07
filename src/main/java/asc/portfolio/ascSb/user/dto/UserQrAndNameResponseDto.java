package asc.portfolio.ascSb.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserQrAndNameResponseDto {

    private String name;
    private String qrCode;

    public UserQrAndNameResponseDto(UserQrAndNameResponseDto entity) {
        this.name = entity.getName();
        this.qrCode = entity.getQrCode();
    }
}