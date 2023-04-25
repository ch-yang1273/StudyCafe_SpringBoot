package asc.portfolio.ascSb.push.controller;

import asc.portfolio.ascSb.common.auth.LoginUser;
import asc.portfolio.ascSb.push.dto.DeviceTokenRegistrationRequest;
import asc.portfolio.ascSb.push.service.DeviceTokenService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/device-token")
public class DeviceTokenController {

    private final DeviceTokenService DeviceTokenService;

    @PostMapping("/register")
    public ResponseEntity<Void> registerToken(@LoginUser Long userId, @RequestBody DeviceTokenRegistrationRequest dto) {
        DeviceTokenService.registerToken(userId, dto.getToken());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/delete")
    public ResponseEntity<Void> deleteToken(@LoginUser Long userId) {
        DeviceTokenService.deleteTokenByUserId(userId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/update")
    public ResponseEntity<Void> updateToken(@LoginUser Long userId, @RequestBody DeviceTokenRegistrationRequest dto) {
        DeviceTokenService.registerToken(userId, dto.getToken());
        return ResponseEntity.ok().build();
    }
}
