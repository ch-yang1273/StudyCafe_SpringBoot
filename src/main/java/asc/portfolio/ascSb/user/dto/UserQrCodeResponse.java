package asc.portfolio.ascSb.user.dto;

import lombok.Getter;

@Getter
public class UserQrCodeResponse {

    private String name;
    private String qrCode;

    // for ObjectMapper
    private UserQrCodeResponse() {
    }

    public UserQrCodeResponse(String name, String qrCode) {
        this.name = name;
        this.qrCode = qrCode;
    }
}