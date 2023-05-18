package asc.portfolio.ascSb.product.service;

import asc.portfolio.ascSb.cafe.domain.Cafe;
import asc.portfolio.ascSb.cafe.domain.CafeFinder;
import asc.portfolio.ascSb.follow.domain.FollowFinder;
import asc.portfolio.ascSb.order.domain.Orders;
import asc.portfolio.ascSb.product.domain.Product;
import asc.portfolio.ascSb.product.domain.ProductRepository;
import asc.portfolio.ascSb.product.domain.ProductStatus;
import asc.portfolio.ascSb.user.domain.User;
import asc.portfolio.ascSb.user.domain.UserFinder;
import asc.portfolio.ascSb.bootpay.dto.BootPayOrderDto;
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
    private final UserFinder userFinder;
    private final FollowFinder followFinder; //todo : 삭제
    private final CafeFinder cafeFinder;

    @Transactional(readOnly = true)
    public List<ProductResponse> adminSalesManagementOneUser(Long adminId, String userLoginId) {
        // admin의 Cafe, 대상 유저의 LoginId로 Product List를 찾고 있다.
        User user = userFinder.findByLoginId(userLoginId);
        Cafe cafe = cafeFinder.findByAdminId(adminId);

        List<Product> list = productRepository.findProductsByUserIdAndCafeName(user.getId(), cafe.getCafeName());
        return list.stream()
                .map(ProductResponse::new)
                .collect(Collectors.toList());
    }

    // todo : BootPay의 PaymentService에서 호출하고 있다. 정리해야 함
    @Transactional(propagation = Propagation.REQUIRED)
    public void saveProduct(Long userId, BootPayOrderDto dto, Orders orders) {
        // todo : 여기 cafe도 user로부터 나올 것이 아니라 주문에서 나와야한다. 수정 필요!
        Cafe cafe = followFinder.findFollowedCafe(userId);

        Product product = Product.builder()
                .cafeId(cafe.getId())
                .userId(userId)
                .productStatus(ProductStatus.SALE)
                .productType(orders.getProductType())
                .price(Math.toIntExact(orders.getOrderPrice()))
                .label(orders.getProductLabel())
                .build();

        productRepository.save(product);
    }

    @Transactional
    public void cancelProduct(String productLabel) {
        Product cancelProductInfo = productRepository.findByLabelContains(productLabel).orElseThrow();
        cancelProductInfo.cancelProduct();
    }
}
