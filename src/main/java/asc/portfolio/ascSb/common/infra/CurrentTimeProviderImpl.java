package asc.portfolio.ascSb.common.infra;

import asc.portfolio.ascSb.common.domain.CurrentTimeProvider;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Component
public class CurrentTimeProviderImpl implements CurrentTimeProvider {

    @Override
    public LocalDateTime localDateTimeNow() {
        return LocalDateTime.now();
    }

    @Override
    public LocalDate localDateNow() {
        return LocalDate.now();
    }

    @Override
    public Date dateNow() {
        Instant instant = localDateTimeNow().atZone(ZoneId.systemDefault()).toInstant();
        return Date.from(instant);
    }
}
