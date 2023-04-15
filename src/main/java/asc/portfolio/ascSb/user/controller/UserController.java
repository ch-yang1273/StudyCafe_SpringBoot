package asc.portfolio.ascSb.user.controller;

import asc.portfolio.ascSb.common.auth.LoginUser;
import asc.portfolio.ascSb.user.domain.TokenPairDto;
import asc.portfolio.ascSb.user.dto.UserLoginRequest;
import asc.portfolio.ascSb.user.dto.UserProfile;
import asc.portfolio.ascSb.user.dto.UserQrCodeResponse;
import asc.portfolio.ascSb.user.dto.UserReissueRequest;
import asc.portfolio.ascSb.user.dto.UserSignupRequest;
import asc.portfolio.ascSb.user.service.UserAuthService;
import asc.portfolio.ascSb.user.service.UserRoleCheckService;
import asc.portfolio.ascSb.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserAuthService userAuthService;
    private final UserRoleCheckService userRoleCheckService;
    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<Void> signup(@RequestBody @Valid UserSignupRequest signupDto) {
        userAuthService.signup(signupDto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<TokenPairDto> login(@RequestBody @Valid UserLoginRequest loginDto) {
        TokenPairDto loginRespDto = userAuthService.checkPassword(loginDto.getLoginId(), loginDto.getPassword());
        return ResponseEntity.ok().body(loginRespDto);
    }

    @PostMapping("/reissue")
    public ResponseEntity<TokenPairDto> reissueToken(@RequestBody @Valid UserReissueRequest tokenRequestDto) {
        return ResponseEntity.ok()
                .body(userAuthService.reissueToken(
                        tokenRequestDto.getAccessToken(),
                        tokenRequestDto.getRefreshToken()));
    }

    @GetMapping("/profile")
    public ResponseEntity<UserProfile> getMyProfile(@LoginUser Long userId) {
        return ResponseEntity.ok().body(userService.getProfileById(userId));
    }

    @GetMapping("/qr")
    public ResponseEntity<UserQrCodeResponse> getQrCode(@LoginUser Long userId) {
        return ResponseEntity.ok().body(userService.userQrAndName(userId));
    }

    @GetMapping("/admin/check")
    public ResponseEntity<UserProfile> getUserInfoByLoginId(@LoginUser Long adminId, @RequestParam String userLoginId) {
        userRoleCheckService.isAdmin(adminId);
        return ResponseEntity.ok().body(userService.getUserInfoByLoginId(userLoginId));
    }

    @GetMapping("/admin/check/user-id")
    public ResponseEntity<Void> checkUserInfoByLoginId(@RequestParam String userLoginId) {
        userService.getUserInfoByLoginId(userLoginId);
        return ResponseEntity.ok().build();
    }
}
