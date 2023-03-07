package asc.portfolio.ascSb.product.service;

import asc.portfolio.ascSb.order.domain.Orders;
import asc.portfolio.ascSb.product.domain.Product;
import asc.portfolio.ascSb.product.domain.ProductRepository;
import asc.portfolio.ascSb.product.domain.ProductStateType;
import asc.portfolio.ascSb.user.domain.User;
import asc.portfolio.ascSb.user.domain.UserRepository;
import asc.portfolio.ascSb.common.infra.bootpay.dto.BootPayOrderDto;
import asc.portfolio.ascSb.product.dto.ProductDto;
import asc.portfolio.ascSb.product.dto.ProductListResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(propagation = Propagation.REQUIRED)
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    private final UserRepository userRepository;

    @Override
    public List<ProductListResponseDto> adminSalesManagementOneUser(String userLoginId, String cafeName) {
        Optional<User> user = userRepository.findByLoginId(userLoginId);
        if(user.isPresent()) {
            User userDto = user.get();
            Long id = userDto.getId();
            return productRepository.findProductListByUserIdAndCafeName(id, cafeName).stream()
                    .map(ProductListResponseDto::new)
                    .collect(Collectors.toList());
        }
        return null;
    }

    @Override
    public List<ProductListResponseDto> adminSalesManagementWithStartDate(String cafeName, String dateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
        LocalDateTime parse = LocalDateTime.parse(dateString, formatter);
        return productRepository.findProductListByUserIdAndCafeNameAndStartTime(cafeName,parse).stream()
                .map(ProductListResponseDto::new)
                .collect(Collectors.toList());
    }

    @Override
    public Product saveProduct(User user, BootPayOrderDto dto, Orders orders) {
        Product productDto = ProductDto.builder()
                .cafe(user.getCafe())
                .user(user)
                .productState(ProductStateType.SALE)
                .productNameType(orders.getOrderProductName())
                .productPrice(Math.toIntExact(orders.getOrderPrice()))
                .productLabel(orders.getProductLabel())
                .build()
                .toEntity();

        return productRepository.save(productDto);
    }

    @Override
    public void cancelProduct(String productLabel) {
        Product cancelProductInfo = productRepository.findByProductLabelContains(productLabel);
        cancelProductInfo.cancelProduct();
        productRepository.save(cancelProductInfo);
    }
}
