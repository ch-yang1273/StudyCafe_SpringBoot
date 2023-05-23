package asc.portfolio.ascSb.product.controller;


import asc.portfolio.ascSb.common.auth.LoginUser;
import asc.portfolio.ascSb.order.service.OrderService;
import asc.portfolio.ascSb.product.dto.ProductResponse;
import asc.portfolio.ascSb.product.service.ProductService;
import asc.portfolio.ascSb.ticket.service.TicketService;
import asc.portfolio.ascSb.user.service.UserRoleCheckService;
import kr.co.bootpay.Bootpay;
import kr.co.bootpay.model.request.Cancel;
import kr.co.bootpay.model.response.ResDefault;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<List<ProductResponse>> getProducts(@LoginUser Long adminId, @RequestParam Long customerId) {
        userRoleCheckService.isAdmin(adminId);
        return ResponseEntity.ok().body(productService.getProducts(adminId, customerId));
    }

    @PostMapping("/admin/management/cancel")
    public ResponseEntity<String> cancelProduct(@LoginUser Long userId, @RequestParam("product-label") String productLabel) {

        final String receiptId = orderService.findReceiptIdToProductLabel(productLabel.substring(11)).getReceiptId();
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

            return ResponseEntity.ok().body("환불완료");

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("환불실패");
        }
    }
}
