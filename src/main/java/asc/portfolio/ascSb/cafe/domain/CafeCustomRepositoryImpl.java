package asc.portfolio.ascSb.cafe.domain;

import asc.portfolio.ascSb.cafe.dto.CafeResponseDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static asc.portfolio.ascSb.cafe.domain.QCafe.*;

@RequiredArgsConstructor
@Repository
public class CafeCustomRepositoryImpl implements CafeCustomRepository {

    private final JPAQueryFactory query;

    @Override
    public List<CafeResponseDto> findAllCafeNameAndArea() {
        return query
                .select(Projections.constructor(CafeResponseDto.class, cafe.cafeName, cafe.cafeArea))
                .from(cafe)
                .orderBy(cafe.cafeArea.asc())
                .fetch();
    }
}
