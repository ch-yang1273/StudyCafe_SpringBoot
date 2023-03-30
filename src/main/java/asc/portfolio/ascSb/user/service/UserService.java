package asc.portfolio.ascSb.user.service;

import asc.portfolio.ascSb.user.dto.UserProfile;
import asc.portfolio.ascSb.user.dto.UserQrCodeResponse;

public interface UserService {

    UserQrCodeResponse userQrAndName(Long id);

    UserProfile getProfileById(Long userID);

    UserProfile getUserInfoByLoginId(String userId);
}
