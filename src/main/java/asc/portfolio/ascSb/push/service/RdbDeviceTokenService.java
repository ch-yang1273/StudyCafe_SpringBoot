package asc.portfolio.ascSb.push.service;

import asc.portfolio.ascSb.common.domain.CurrentTimeProvider;
import asc.portfolio.ascSb.push.domain.DeviceToken;
import asc.portfolio.ascSb.push.domain.DeviceTokenRepository;
import asc.portfolio.ascSb.push.exception.PushErrorData;
import asc.portfolio.ascSb.push.exception.PushException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@RequiredArgsConstructor
@Service
public class RdbDeviceTokenService implements DeviceTokenService {

    private final DeviceTokenRepository deviceTokenRepository;
    private final CurrentTimeProvider currentTimeProvider;

    @Transactional
    @Override
    public void registerToken(Long userId, String token) {
        LocalDate expiredAt = currentTimeProvider.localDateNow();

        if (deviceTokenRepository.existsById(userId)) {
            updateToken(userId, token, expiredAt);
        }
        DeviceToken entity = DeviceToken.builder()
                .userId(userId)
                .token(token)
                .expiredAt(expiredAt)
                .build();
        deviceTokenRepository.save(entity);
    }

    @Transactional
    @Override
    public void deleteToken(String token) {
        deviceTokenRepository.findByToken(token).ifPresent(deviceTokenRepository::delete);
    }

    @Transactional
    @Override
    public void deleteTokenByUserId(Long userId) {
        if (!deviceTokenRepository.existsById(userId)) {
            return;
        }
        deviceTokenRepository.deleteById(userId);
    }

    @Override
    @Transactional
    public void deleteExpiredToken(LocalDate now) {
        deviceTokenRepository.deleteExpiredToken(now);
    }

    private void updateToken(Long userId, String token, LocalDate expiredAt) {
        DeviceToken deviceToken = deviceTokenRepository.findById(userId).orElseThrow(
                () -> new PushException(PushErrorData.TOKEN_NOT_FOUND)
        );
        deviceToken.updateToken(token, expiredAt);
    }

    @Transactional(readOnly = true)
    @Override
    public String getToken(Long userId) {
        DeviceToken deviceToken = deviceTokenRepository.findById(userId).orElseThrow(
                () -> new PushException(PushErrorData.TOKEN_NOT_FOUND)
        );
        return deviceToken.getToken();
    }
}
