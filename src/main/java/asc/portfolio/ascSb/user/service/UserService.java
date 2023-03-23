package asc.portfolio.ascSb.user.service;

import asc.portfolio.ascSb.user.dto.UserProfileDto;
import asc.portfolio.ascSb.user.dto.UserQrAndNameResponseDto;
import org.springframework.transaction.annotation.Transactional;

public interface UserService {

    UserQrAndNameResponseDto userQrAndName(Long id);

    @Transactional(readOnly = true)
    UserProfileDto getProfileById(Long userID);

    UserProfileDto getUserInfoByLoginId(String userId);

    void updateUserCafe(Long userId, Long cafeId);
}
