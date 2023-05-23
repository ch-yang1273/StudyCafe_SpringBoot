package asc.portfolio.ascSb.product.service;

import asc.portfolio.ascSb.order.domain.Orders;
import asc.portfolio.ascSb.product.domain.Product;
import asc.portfolio.ascSb.product.domain.ProductFinder;
import asc.portfolio.ascSb.product.domain.ProductRepository;
import asc.portfolio.ascSb.product.domain.ProductStatus;
import asc.portfolio.ascSb.product.dto.ProductResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductFinder productFinder;

    @Transactional(readOnly = true)
    public List<ProductResponse> getProducts(Long adminId, Long customerId) {
        List<Product> list = productFinder.findCustomerProductsInAdminCafe(adminId, customerId);
        return list.stream()
                .map(ProductResponse::new)
                .collect(Collectors.toList());
    }

    // todo : BootPay의 PaymentService에서 호출하고 있다. 정리해야 함
    @Transactional(propagation = Propagation.REQUIRED)
    public void saveProduct(Long userId, Orders orders) {

        Product product = Product.builder()
                .userId(userId)
                .cafeId(orders.getId())
                .status(ProductStatus.SALE)
                .type(orders.getProductType())
                .price(orders.getPrice())
                .label(orders.getProductLabel())
                .build();

        productRepository.save(product);
    }

    @Transactional
    public void cancelProduct(Long id) {
        Product product = productFinder.findById(id);
        product.cancelProduct();
    }
}
