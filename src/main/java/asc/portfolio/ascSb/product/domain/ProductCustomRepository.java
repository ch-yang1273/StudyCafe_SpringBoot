package asc.portfolio.ascSb.product.domain;

import java.util.List;

public interface ProductCustomRepository {

    List<Product> findProductsByUserIdAndCafeId(Long userId, Long cafeId);
}
