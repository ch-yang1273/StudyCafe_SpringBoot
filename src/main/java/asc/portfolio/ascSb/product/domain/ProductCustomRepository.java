package asc.portfolio.ascSb.product.domain;

import java.util.List;

public interface ProductCustomRepository {

    List<Product> findProductsByUserIdAndCafeName(Long userId, String cafeName);
}
