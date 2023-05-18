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
    public List<Product> findProductsByUserIdAndCafeId(Long userId, Long cafeId) {
        return query.select(QProduct.product)
                .from(QProduct.product)
                .where(QProduct.product.userId.eq(userId), QProduct.product.cafeId.eq(cafeId))
                .fetch();
    }
}
