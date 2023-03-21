package asc.portfolio.ascSb.cafe.domain;

import asc.portfolio.ascSb.config.querydslconfig.TestQueryDslConfig;
import asc.portfolio.ascSb.support.cafe.CafeFixture;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import({TestQueryDslConfig.class, CafeFinder.class})
class CafeRepositoryTest {
    //todo : CafeFinder.class는 왜 필요한지 모르겠네...

    private final CafeRepository cafeRepository;

    @Autowired
    public CafeRepositoryTest(CafeRepository cafeRepository) {
        this.cafeRepository = cafeRepository;
    }

    @Test
    void cafeEntity() {
        Cafe cafe = CafeFixture.CAFE_A.toCafe();
        cafeRepository.save(cafe);
    }
}