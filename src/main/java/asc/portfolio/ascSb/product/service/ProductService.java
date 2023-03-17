package asc.portfolio.ascSb.product.service;

import asc.portfolio.ascSb.order.domain.Orders;
import asc.portfolio.ascSb.common.infra.bootpay.dto.BootPayOrderDto;
import asc.portfolio.ascSb.product.dto.ProductListResponseDto;

import java.util.List;

public interface ProductService {

    List<ProductListResponseDto> adminSalesManagementOneUser(Long userId);

    List<ProductListResponseDto> adminSalesManagementWithStartDate(String cafeName, String dateString);

    void saveProduct(Long userId, BootPayOrderDto dto, Orders orders);

    void cancelProduct(String productLabel);
}
