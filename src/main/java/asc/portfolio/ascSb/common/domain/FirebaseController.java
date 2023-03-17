package asc.portfolio.ascSb.common.domain;

import asc.portfolio.ascSb.common.auth.LoginUser;
import asc.portfolio.ascSb.common.infra.fcm.service.FirebaseCloudMessageService;
import asc.portfolio.ascSb.common.infra.fcm.service.fcmtoken.FCMTokenService;
import asc.portfolio.ascSb.common.infra.fcm.dto.FCMRequestDto;
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

    private final FirebaseCloudMessageService firebaseCloudMessageService;

    @PostMapping("/cm-token/confirm")
    public ResponseEntity<?> checkFCMToken(@LoginUser Long userId, @RequestParam String fCMToken) {
        //todo : USER Role Check
        Boolean confirm = fcmTokenService.confirmToken(userId, fCMToken);
        if (confirm)
            return new ResponseEntity<>("OK", HttpStatus.OK);

        //todo : ADMIN 용은 따로 만들어야 함.
//        if (user.getRole() == UserRoleType.ADMIN) {
//            Long id = fcmTokenService.confirmAdminFCMToken(user, fCMToken);
//            if (id == null) {
//                log.info("admin fcm token verify failed");
//                return new ResponseEntity<>("FAIL", HttpStatus.BAD_REQUEST);
//            }
//            return new ResponseEntity<>("OK", HttpStatus.OK);
//        }

        log.info("fcm token check error");
        return  new ResponseEntity<>("FAIL", HttpStatus.BAD_REQUEST);
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
