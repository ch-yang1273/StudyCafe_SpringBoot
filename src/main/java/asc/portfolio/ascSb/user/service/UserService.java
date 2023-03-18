package asc.portfolio.ascSb.user.service;

import asc.portfolio.ascSb.user.dto.UserProfileDto;
import asc.portfolio.ascSb.user.dto.UserQrAndNameResponseDto;

public interface UserService {

    UserQrAndNameResponseDto userQrAndName(Long id);

    UserProfileDto getUserInfoByLoginId(String userId);

    void updateUserCafe(Long userId, Long cafeId);
}
