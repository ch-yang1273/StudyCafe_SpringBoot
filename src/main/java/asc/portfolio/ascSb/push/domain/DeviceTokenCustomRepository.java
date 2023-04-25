package asc.portfolio.ascSb.push.domain;

import java.time.LocalDate;

public interface DeviceTokenCustomRepository {

    void deleteExpiredToken(LocalDate now);
}
