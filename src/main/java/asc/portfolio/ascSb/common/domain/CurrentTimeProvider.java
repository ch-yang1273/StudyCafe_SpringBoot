package asc.portfolio.ascSb.common.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

public interface CurrentTimeProvider {

    LocalDateTime localDateTimeNow();

    LocalDate localDateNow();

    Date dateNow();
}
