package asc.portfolio.ascSb.product.controller;

import asc.portfolio.ascSb.common.auth.LoginUser;
import asc.portfolio.ascSb.product.dto.ProductResponse;
import asc.portfolio.ascSb.product.service.ProductService;
import asc.portfolio.ascSb.user.service.UserRoleCheckService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/product")
public class ProductController {

    private final ProductService productService;
    private final UserRoleCheckService userRoleCheckService;

    @GetMapping("/admin/management")
    public ResponseEntity<List<ProductResponse>> getProducts(@LoginUser Long adminId, @RequestParam Long customerId) {
        userRoleCheckService.isAdmin(adminId);
        return ResponseEntity.ok().body(productService.getProducts(adminId, customerId));
    }
}
