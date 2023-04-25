package asc.portfolio.ascSb.push.domain;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@RequiredArgsConstructor
@Repository
public class DeviceTokenCustomRepositoryImpl implements DeviceTokenCustomRepository {

    private final JPAQueryFactory query;

    @Override
    public void deleteExpiredToken(LocalDate now) {
        query.delete(QDeviceToken.deviceToken)
                .where(QDeviceToken.deviceToken.expiredAt.before(now))
                .execute();
    }
}
