package asc.portfolio.ascSb.product.domain;

import asc.portfolio.ascSb.cafe.domain.QCafe;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

import static asc.portfolio.ascSb.cafe.domain.QCafe.*;
import static asc.portfolio.ascSb.product.domain.QProduct.*;

@RequiredArgsConstructor
@Repository
@Getter
public class ProductCustomRepositoryImpl implements ProductCustomRepository {

    private final JPAQueryFactory query;

    @Override
    public List<Product> findProductsByUserIdAndCafeNameAndStartTime(String cafeName, LocalDateTime startTime) {
        LocalDateTime now = LocalDateTime.now();
        return query
                .select(product)
                .from(product)
                .leftJoin(product.cafe, cafe)
                .where(cafe.cafeName.eq(cafeName),
                        product.createDate.between(startTime, now),
                        product.productState.eq(ProductStateType.SALE)
                )
                .fetch();
    }

    @Override
    public List<Product> findProductsByUserIdAndCafeName(Long userId, String cafeName) {
        QProduct qProduct = new QProduct("subQ");
        QCafe qCafe = new QCafe("subC");

        return query.select(qProduct)
                .from(qProduct)
                .leftJoin(qProduct.cafe, qCafe)
                .where(qProduct.user.id.eq(userId), qCafe.cafeName.eq(cafeName))
                .fetch();
    }
}
