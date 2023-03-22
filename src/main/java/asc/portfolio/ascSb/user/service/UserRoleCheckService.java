package asc.portfolio.ascSb.user.service;

import asc.portfolio.ascSb.user.domain.User;
import asc.portfolio.ascSb.user.domain.UserFinder;
import asc.portfolio.ascSb.user.domain.UserRoleType;
import asc.portfolio.ascSb.user.exception.UserErrorData;
import asc.portfolio.ascSb.user.exception.UserException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Component
public class UserRoleCheckService {

    private final UserFinder userFinder;

    private void checkRole(Long userId, UserRoleType roleType) {
        User user = userFinder.findById(userId);

        if (user.getRole() != roleType) {
            switch (user.getRole()) {
                case USER:
                    throw new UserException(UserErrorData.USER_NEED_USER_ROLE);
                case ADMIN:
                    throw new UserException(UserErrorData.USER_NEED_ADMIN_ROLE);
                default:
                    throw new IllegalStateException("알 수 없는 유저 권한");
            }
        }
    }

    @Transactional(readOnly = true)
    public void isAdmin(Long adminId) {
        checkRole(adminId, UserRoleType.ADMIN);
    }

    @Transactional(readOnly = true)
    public void isUser(Long userId) {
        checkRole(userId, UserRoleType.USER);
    }
}
