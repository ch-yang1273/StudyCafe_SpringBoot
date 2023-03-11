package asc.portfolio.ascSb.common.domain;

import java.time.LocalDateTime;
import java.util.Date;

public interface CurrentTimeProvider {

    public LocalDateTime now();

    public Date toDate(LocalDateTime localDateTime);
}
