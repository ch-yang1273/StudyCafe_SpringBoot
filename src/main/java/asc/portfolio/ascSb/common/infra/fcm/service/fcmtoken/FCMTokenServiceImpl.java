package asc.portfolio.ascSb.common.infra.fcm.service.fcmtoken;

import asc.portfolio.ascSb.adminfcmtoken.domain.AdminFCMToken;
import asc.portfolio.ascSb.adminfcmtoken.domain.AdminFCMTokenRepository;
import asc.portfolio.ascSb.common.infra.redis.RedisRepository;
import asc.portfolio.ascSb.user.domain.User;
import asc.portfolio.ascSb.common.infra.fcm.dto.fcmtoken.AdminFCMTokenRequestDto;
import asc.portfolio.ascSb.user.domain.UserRepository;
import asc.portfolio.ascSb.user.exception.UnknownUserException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class FCMTokenServiceImpl implements FCMTokenService {

    private final AdminFCMTokenRepository adminFCMTokenRepository;

    private final RedisRepository redisRepository;

    private final UserRepository userRepository;

    // 관리자가 FCM 토큰을 이미 가지고 있는지, 가지고 있으면 유효한 토큰인지 검사
    @Override
    public Boolean isAdminHasToken(User user, String adminFCMToken) {
        Optional<AdminFCMToken> token = adminFCMTokenRepository.findById(user.getId());
        if (token.isPresent()) {
            AdminFCMToken existToken = token.get();
            String pastToken = token.get().getFCMToken();
            if (!Objects.equals(pastToken, adminFCMToken)) {
                // 이미 존재하는 토큰을 현재 유효한 토큰으로 바꾼다
                existToken.setFCMToken(adminFCMToken);
                adminFCMTokenRepository.save(existToken);
                return true;
            }
            Optional<AdminFCMToken> fcmToken = adminFCMTokenRepository.findById(user.getId());
            if (fcmToken.isPresent()) {
                String checkDistinct = fcmToken.get().getFCMToken();
                return Objects.equals(checkDistinct, adminFCMToken);
                }
            }
            return false;
        }

    // FCM Token을 ADMIN_FCM_TOKEN 테이블에 저장 (Redis X) , 관리자 토큰은 RDBMS 에 별도 저장
    @Override
    public Long confirmAdminFCMToken (User user, String adminFCMToken) throws IllegalArgumentException {

        Boolean validation = isAdminHasToken(user, adminFCMToken);

        if (!validation) {
            AdminFCMToken adminFCMTokenRequestDto =
                    AdminFCMTokenRequestDto.builder()
                            .user(user)
                            .cafe(user.getCafe())
                            .fCMToken(adminFCMToken)
                            .build()
                            .toEntity();
            try {
                Long saveId = adminFCMTokenRepository.save(adminFCMTokenRequestDto).getId();
                return saveId;
            } catch (IllegalArgumentException e) {
                log.info("fcm token save failed");
                e.printStackTrace();
                return null;
            }
        }
        // 이미 유효한 토큰을 가짐, save 필요 X
        return 1L;
    }

    // user 의 FCM 토큰을 Redis 에 저장
    @Override
    public Boolean confirmToken (Long userId, String userFCMToken) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UnknownUserException());

        String userKeyType = user.getLoginId() + "_" + user.getRole() + "_FCM_TOKEN"; // "${userLoginId}_${userRole}_FCM_TOKEN"
        Long timeOutDay = 30L * 24L * 3600L; // 30일

        // 이미 토큰을 가지고 있는지 확인
        Boolean isDuplicated = redisRepository.hasKey(userKeyType);

        // 새로운 토큰 등록
        if (!isDuplicated) {
            redisRepository.saveValue(userKeyType, userFCMToken, timeOutDay);
            log.info("insert new FCM TOKEN");
            return true;
        }

        // 이미 토큰이 존재하지만 받아온 토큰과 일치하지 않는경우
        String value = redisRepository.getValue(userKeyType);

        if (!Objects.equals(value, userFCMToken)) {
            // 기존의 토큰 삭제 후 새로운 토큰으로 저장
            redisRepository.deleteValue(userKeyType);
            redisRepository.saveValue(userKeyType, userFCMToken, timeOutDay);
            log.info("Delete past token and insert new FCM TOKEN");
            return true;
        }

        log.info("complete");
        return true;
    }

    @Override
    public String adminFindSpecificToken(String userId) {
        String token = redisRepository.getValue(userId + "_" + "USER" + "_FCM_TOKEN");
        if (token != null) {
            return token;
        }
        return null;
    }
}
