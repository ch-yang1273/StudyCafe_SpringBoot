package asc.portfolio.ascSb.product.domain;

import asc.portfolio.ascSb.cafe.domain.Cafe;
import asc.portfolio.ascSb.cafe.domain.CafeFinder;
import asc.portfolio.ascSb.product.exception.ProductErrorData;
import asc.portfolio.ascSb.product.exception.ProductException;
import asc.portfolio.ascSb.user.domain.User;
import asc.portfolio.ascSb.user.domain.UserFinder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class ProductFinder {

    private final ProductRepository productRepository;
    private final UserFinder userFinder;
    private final CafeFinder cafeFinder;

    public Product findById(Long productId) {
        return productRepository.findById(productId).orElseThrow(
                () -> new ProductException(ProductErrorData.PRODUCT_NOT_FOUND)
        );
    }

    public List<Product> findCustomerProductsInAdminCafe(Long adminId, Long customerId) {
        User customer = userFinder.findById(customerId);
        Cafe adminCafe = cafeFinder.findByAdminId(adminId);

        return productRepository.findProductsByCafIdAndCustomerId(adminCafe.getId(), customer.getId());
    }
}
