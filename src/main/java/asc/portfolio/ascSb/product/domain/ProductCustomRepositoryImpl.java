package asc.portfolio.ascSb.product.domain;

import asc.portfolio.ascSb.cafe.domain.QCafe;
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
    public List<Product> findProductsByUserIdAndCafeName(Long userId, String cafeName) {
        return query.select(QProduct.product)
                .from(QProduct.product)
                .join(QProduct.product.cafe, QCafe.cafe)
                .where(QProduct.product.user.id.eq(userId), QCafe.cafe.cafeName.eq(cafeName))
                .fetch();
    }
}
