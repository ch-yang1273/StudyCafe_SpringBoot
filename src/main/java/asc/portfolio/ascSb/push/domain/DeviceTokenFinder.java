package asc.portfolio.ascSb.push.domain;

import asc.portfolio.ascSb.push.exception.PushErrorData;
import asc.portfolio.ascSb.push.exception.PushException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class DeviceTokenFinder {

    private final DeviceTokenRepository deviceTokenRepository;

    public DeviceToken findById(Long id) {
         return deviceTokenRepository.findById(id).orElseThrow(
                () -> new PushException(PushErrorData.TOKEN_NOT_FOUND)
        );
    }
}
