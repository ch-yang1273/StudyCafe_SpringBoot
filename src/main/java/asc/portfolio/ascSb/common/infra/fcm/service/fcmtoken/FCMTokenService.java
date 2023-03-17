package asc.portfolio.ascSb.common.infra.fcm.service.fcmtoken;

public interface FCMTokenService {

    Boolean isAdminHasToken(Long adminId, String adminFCMToken);

    Long confirmAdminFCMToken(Long adminId, String adminFCMToken);

    Boolean confirmToken(Long userId, String userFCMToken);

    String adminFindSpecificToken(String userId);
}
