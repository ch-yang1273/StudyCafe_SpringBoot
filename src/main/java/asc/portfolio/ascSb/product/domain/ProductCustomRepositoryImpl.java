package asc.portfolio.ascSb.product.domain;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
@Getter
public class ProductCustomRepositoryImpl implements ProductCustomRepository {

    private final JPAQueryFactory query;

    @Override
    public List<Product> findProductsByCafIdAndCustomerId(Long cafeId, Long customerId) {
        return query.select(QProduct.product)
                .from(QProduct.product)
                .where(QProduct.product.cafeId.eq(cafeId), QProduct.product.userId.eq(customerId))
                .fetch();
    }
}
