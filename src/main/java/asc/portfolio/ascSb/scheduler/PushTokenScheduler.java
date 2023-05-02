package asc.portfolio.ascSb.scheduler;

import asc.portfolio.ascSb.common.domain.CurrentTimeProvider;
import asc.portfolio.ascSb.push.service.DeviceTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class PushTokenScheduler {

    private final DeviceTokenService deviceTokenService;
    private final CurrentTimeProvider currentTimeProvider;

    @Scheduled(cron = "0 0 0 * * ?")
    public void deleteExpiredToken() {
        deviceTokenService.deleteExpiredToken(currentTimeProvider.localDateNow());
    }
}
