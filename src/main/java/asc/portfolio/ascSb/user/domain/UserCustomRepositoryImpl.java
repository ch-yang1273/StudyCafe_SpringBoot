package asc.portfolio.ascSb.user.domain;

import asc.portfolio.ascSb.user.dto.UserQrAndNameResponseDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static asc.portfolio.ascSb.user.domain.QUser.user;

@RequiredArgsConstructor
@Repository
public class UserCustomRepositoryImpl implements UserCustomRepository {

    private final JPAQueryFactory query;

    @Override
    public UserQrAndNameResponseDto findQrAndUserNameById(Long id) {
        return query
                .select(Projections.bean(UserQrAndNameResponseDto.class, user.name, user.qrCode))
                .from(user)
                .where(user.id.eq(id))
                .fetchOne();
    }

}