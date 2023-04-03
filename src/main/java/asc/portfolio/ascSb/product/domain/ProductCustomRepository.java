package asc.portfolio.ascSb.product.domain;

import java.time.LocalDateTime;
import java.util.List;

public interface ProductCustomRepository {

    List<Product> findProductsByUserIdAndCafeNameAndStartTime(String cafeName, LocalDateTime startTime);

    List<Product> findProductsByUserIdAndCafeName(Long userId, String cafeName);

}
