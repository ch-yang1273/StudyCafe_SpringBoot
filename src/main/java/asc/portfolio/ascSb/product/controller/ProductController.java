package asc.portfolio.ascSb.product.controller;


import asc.portfolio.ascSb.bootpay.domain.Bootpay;
import asc.portfolio.ascSb.common.auth.LoginUser;
import asc.portfolio.ascSb.order.service.OrderService;
import asc.portfolio.ascSb.product.service.ProductService;
import asc.portfolio.ascSb.ticket.service.TicketService;
import asc.portfolio.ascSb.product.dto.ProductListResponseDto;
import asc.portfolio.ascSb.user.service.UserRoleCheckService;
import kr.co.bootpay.model.request.Cancel;
import kr.co.bootpay.model.response.ResDefault;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/product")
public class ProductController {

    private final ProductService productService;
    private final UserRoleCheckService userRoleCheckService;
    private final OrderService orderService;
    private final TicketService ticketService;

    @GetMapping("/admin/management")
    public ResponseEntity<List<ProductListResponseDto>> productInfoOneUser(@LoginUser Long adminId, @RequestParam String userLoginId) {
        userRoleCheckService.isAdmin(adminId);
        return new ResponseEntity<>(productService.adminSalesManagementOneUser(adminId, userLoginId), HttpStatus.OK);
    }

    @GetMapping("/admin/management/start-time/{cafeName}")
    public ResponseEntity<List<ProductListResponseDto>> productInfoWithConstTerm(
            @LoginUser Long userId,
            @PathVariable String cafeName,
            @RequestHeader(value = "dateString") String dateString) {
        userRoleCheckService.isAdmin(userId);
        return new ResponseEntity<>(productService.adminSalesManagementWithStartDate(cafeName, dateString), HttpStatus.OK);
    }

    @PostMapping("/admin/management/cancel/product")
    public ResponseEntity<String> cancelOneProduct(@LoginUser Long userId, @RequestParam("product-label") String productLabel) {

        final String receiptId = orderService.findReceiptIdToProductLabel(productLabel.substring(11)).getReceiptOrderId();
        userRoleCheckService.isAdmin(userId);

        String rest_application_id = "";
        String private_key = "";

        Bootpay api = new Bootpay(
                rest_application_id,
                private_key
        );

        try {
            ResDefault<HashMap<String, Object>> token = api.getAccessToken();
            ResDefault<HashMap<String, Object>> verify = api.verify(receiptId);
            Cancel cancel = new Cancel();
            cancel.receiptId = receiptId;
            cancel.name = "관리자";
            cancel.reason = "유저 결제 환불 처리";

            ResDefault<HashMap<String, Object>> res = api.receiptCancel(cancel);
            log.info(res.message);
            // 환불한 product productState Enum을 Cancel로 처리
            productService.cancelProduct(productLabel.substring(11));
            // 환불한 티켓을 Invalid 처리
            ticketService.setInvalidTicket(productLabel.substring(11));

            return new ResponseEntity<>("환불완료", HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("환불실패",HttpStatus.BAD_GATEWAY);
        }
    }
}
