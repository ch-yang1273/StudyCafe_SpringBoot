package asc.portfolio.ascSb.product.domain;

import java.util.List;

public interface ProductCustomRepository {

    List<Product> findProductsByCafIdAndCustomerId(Long cafeId, Long customerId);
}
