package asc.portfolio.ascSb.user.dto;

import lombok.Getter;

@Getter
public class UserQrAndNameResponseDto {

    private String name;
    private String qrCode;

    // for ObjectMapper
    private UserQrAndNameResponseDto() {
    }

    public UserQrAndNameResponseDto(String name, String qrCode) {
        this.name = name;
        this.qrCode = qrCode;
    }
}