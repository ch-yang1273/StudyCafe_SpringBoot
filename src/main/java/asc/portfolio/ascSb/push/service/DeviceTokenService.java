package asc.portfolio.ascSb.push.service;

import java.time.LocalDate;

public interface DeviceTokenService {

    void registerToken(Long userId, String token);

    void deleteToken(String token);

    void deleteTokenByUserId(Long userId);

    void deleteExpiredToken(LocalDate now);

    String getToken(Long userId);
}
