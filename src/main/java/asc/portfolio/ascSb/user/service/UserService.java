package asc.portfolio.ascSb.user.service;

import asc.portfolio.ascSb.user.dto.UserProfileDto;
import asc.portfolio.ascSb.user.dto.UserQrAndNameResponseDto;

public interface UserService {

    UserQrAndNameResponseDto userQrAndName(Long id);

    UserProfileDto getProfileById(Long userID);

    UserProfileDto getUserInfoByLoginId(String userId);
}
