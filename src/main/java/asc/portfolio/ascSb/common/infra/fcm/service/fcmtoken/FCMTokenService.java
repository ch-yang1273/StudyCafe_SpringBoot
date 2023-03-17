package asc.portfolio.ascSb.common.infra.fcm.service.fcmtoken;

import asc.portfolio.ascSb.user.domain.User;

public interface FCMTokenService {
    Long confirmAdminFCMToken(User user, String adminFCMToken);
    Boolean isAdminHasToken(User user, String adminFCMToken);
    Boolean confirmToken(Long userId, String userFCMToken);
    String adminFindSpecificToken(String userId);
}
