package asc.portfolio.ascSb.cafe.domain;

import asc.portfolio.ascSb.config.querydslconfig.TestQueryDslConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import(TestQueryDslConfig.class)
class CafeRepositoryTest {

    private final CafeRepository cafeRepository;

    @Autowired
    public CafeRepositoryTest(CafeRepository cafeRepository) {
        this.cafeRepository = cafeRepository;
    }

    @Test
    void cafeEntity() {
        Cafe cafe = Cafe.builder()
                .cafeName("test")
                .cafeArea("서울")
                .build();

        cafeRepository.save(cafe);
    }
}