package asc.portfolio.ascSb.product.service;

import asc.portfolio.ascSb.order.domain.Orders;
import asc.portfolio.ascSb.product.domain.Product;
import asc.portfolio.ascSb.user.domain.User;
import asc.portfolio.ascSb.common.infra.bootpay.dto.BootPayOrderDto;
import asc.portfolio.ascSb.product.dto.ProductListResponseDto;

import java.util.List;

public interface ProductService {

    List<ProductListResponseDto> adminSalesManagementOneUser(String userLoginId, String cafeName);

    List<ProductListResponseDto> adminSalesManagementWithStartDate(String cafeName, String dateString);

    Product saveProduct(User user, BootPayOrderDto dto, Orders orders);

    void cancelProduct(String productLabel);

}
