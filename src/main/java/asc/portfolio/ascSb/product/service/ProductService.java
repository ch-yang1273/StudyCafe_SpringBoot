package asc.portfolio.ascSb.product.service;

import asc.portfolio.ascSb.cafe.domain.Cafe;
import asc.portfolio.ascSb.cafe.domain.CafeFinder;
import asc.portfolio.ascSb.follow.domain.FollowFinder;
import asc.portfolio.ascSb.order.domain.Orders;
import asc.portfolio.ascSb.product.domain.Product;
import asc.portfolio.ascSb.product.domain.ProductRepository;
import asc.portfolio.ascSb.product.domain.ProductStateType;
import asc.portfolio.ascSb.user.domain.User;
import asc.portfolio.ascSb.user.domain.UserFinder;
import asc.portfolio.ascSb.bootpay.dto.BootPayOrderDto;
import asc.portfolio.ascSb.product.dto.ProductDto;
import asc.portfolio.ascSb.product.dto.ProductResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(propagation = Propagation.REQUIRED)
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;
    private final UserFinder userFinder;
    private final FollowFinder followFinder; //todo : 삭제
    private final CafeFinder cafeFinder;

    public List<ProductResponse> adminSalesManagementOneUser(Long adminId, String userLoginId) {
        // admin의 Cafe, 대상 유저의 LoginId로 Product List를 찾고 있다.
        User user = userFinder.findByLoginId(userLoginId);
        Cafe cafe = cafeFinder.findByAdminId(adminId);

        List<Product> list = productRepository.findProductsByUserIdAndCafeName(user.getId(), cafe.getCafeName());
        return list.stream()
                .map(ProductResponse::new)
                .collect(Collectors.toList());
    }

    public List<ProductResponse> adminSalesManagementWithStartDate(String cafeName, String dateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
        LocalDateTime parse = LocalDateTime.parse(dateString, formatter);
        return productRepository.findProductsByUserIdAndCafeNameAndStartTime(cafeName,parse).stream()
                .map(ProductResponse::new)
                .collect(Collectors.toList());
    }

    // BootPay의 PaymentService에서 호출하고 있다.
    public void saveProduct(Long userId, BootPayOrderDto dto, Orders orders) {
        // todo : 여기 cafe도 user로부터 나올 것이 아니라 주문에서 나와야한다. 수정 필요!
        User user = userFinder.findById(userId);
        Cafe cafe = followFinder.findFollowedCafe(userId);

        Product product = ProductDto.builder()
                .cafe(cafe)
                .user(user)
                .productState(ProductStateType.SALE)
                .productNameType(orders.getOrderProductNameType())
                .productPrice(Math.toIntExact(orders.getOrderPrice()))
                .productLabel(orders.getProductLabel())
                .build()
                .toEntity();
        productRepository.save(product);
    }

    public void cancelProduct(String productLabel) {
        Product cancelProductInfo = productRepository.findByProductLabelContains(productLabel);
        cancelProductInfo.cancelProduct();
        productRepository.save(cancelProductInfo);
    }
}
