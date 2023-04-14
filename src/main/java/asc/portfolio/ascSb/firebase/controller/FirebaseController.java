package asc.portfolio.ascSb.firebase.controller;

import asc.portfolio.ascSb.common.auth.LoginUser;
import asc.portfolio.ascSb.firebase.service.FirebaseCloudMessageService;
import asc.portfolio.ascSb.firebase.service.fcmtoken.FCMTokenService;
import asc.portfolio.ascSb.firebase.dto.FCMRequestDto;
import asc.portfolio.ascSb.user.service.UserRoleCheckService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/firebase")
public class FirebaseController {

    private final FCMTokenService fcmTokenService;
    private final UserRoleCheckService userRoleCheckService;
    private final FirebaseCloudMessageService firebaseCloudMessageService;

    @PostMapping("/cm-token/confirm")
    public ResponseEntity<?> checkFCMToken(@LoginUser Long userId, @RequestParam String fCMToken) {
        userRoleCheckService.isUser(userId);
        if (fcmTokenService.confirmToken(userId, fCMToken)) {
            return new ResponseEntity<>("OK", HttpStatus.OK);
        } else {
            return  new ResponseEntity<>("FAIL", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/cm-token/admin/confirm")
    public ResponseEntity<String> checkAdminFcmToken(@LoginUser Long adminId, @RequestParam String fCMToken) {
        userRoleCheckService.isAdmin(adminId);

        //todo : 반환 된 id 체크 없이 throw하는 방식으로 변경
        Long id = fcmTokenService.confirmAdminFCMToken(adminId, fCMToken);
        if (id == null) {
            log.info("admin fcm token verify failed");
            return new ResponseEntity<>("FAIL", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }

    @PostMapping("/send/cloud-message")
    public ResponseEntity<?> pushMessage(@RequestBody FCMRequestDto requestDTO) throws IOException {

        log.info("FCM SEND {} {}", requestDTO.getTitle(), requestDTO.getBody());

        String targetToken = fcmTokenService.adminFindSpecificToken(requestDTO.getUser_name());

        firebaseCloudMessageService.sendMessageToSpecificUser(
                targetToken,
                requestDTO.getTitle(),
                requestDTO.getBody());

        return new ResponseEntity<>("OK", HttpStatus.OK);

    }
}