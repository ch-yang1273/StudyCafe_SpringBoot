package asc.portfolio.ascSb.web.controller;


import asc.portfolio.ascSb.bootpay.Bootpay;
import asc.portfolio.ascSb.domain.order.Orders;
import asc.portfolio.ascSb.domain.product.Product;
import asc.portfolio.ascSb.domain.user.User;
import asc.portfolio.ascSb.domain.user.UserRoleType;
import asc.portfolio.ascSb.jwt.LoginUser;
import asc.portfolio.ascSb.service.order.OrderService;
import asc.portfolio.ascSb.service.product.ProductService;
import asc.portfolio.ascSb.service.ticket.TicketService;
import asc.portfolio.ascSb.web.dto.bootpay.BootPayOrderDto;
import asc.portfolio.ascSb.web.dto.product.ProductListResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
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
@CrossOrigin(origins = "*")
@RequestMapping("/api/v1/product")
public class ProductController {

    private final ProductService productService;

    private final OrderService orderService;

    private final TicketService ticketService;

    @RequestMapping(value = "/admin/management", method = RequestMethod.GET)
    public ResponseEntity<List<ProductListResponseDto>> productInfoOneUser(@LoginUser User user, @RequestParam String userLoginId) {
        if(user.getRole() != UserRoleType.ADMIN) {
            log.error("관리자 계정이 아닙니다.");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(productService.adminSalesManagementOneUser(userLoginId, user.getCafe().getCafeName()), HttpStatus.OK);
    }

    @RequestMapping(value = "/admin/management/start-time/{cafeName}", method = RequestMethod.GET)
    public ResponseEntity<List<Product>> productInfoWithConstTerm(@LoginUser User user, @PathVariable String cafeName,
                                                                   @RequestHeader(value = "dateString") String dateString) {
        if(user.getRole() != UserRoleType.ADMIN) {
            log.error("관리자 계정이 아닙니다.");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(productService.adminSalesManagementWithStartDate(cafeName, dateString), HttpStatus.OK);
    }

    @RequestMapping(value = "/admin/management/cancel/product", method = RequestMethod.POST)
    public ResponseEntity<String> cancelOneProduct(@LoginUser User user, @RequestParam("product-label") String productLabel) throws Exception {

        final String receiptId = orderService.findReceiptIdToProductLabel(productLabel.substring(11)).getReceiptOrderId();

        if(user.getRole() != UserRoleType.ADMIN) {
            return new ResponseEntity<>("관리자 계정이 아닙니다",HttpStatus.BAD_REQUEST);
        }

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
            productService.cancelProduct(orderService.findReceiptIdToProductLabel(receiptId).getProductLabel()); // 취소한 product productState Enum을 Cancel로 처리
            ticketService.deleteTicket(orderService.findReceiptIdToProductLabel(receiptId).getProductLabel()); // 환불한 티켓을 삭제

            return new ResponseEntity<>("환불완료", HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("환불실패",HttpStatus.BAD_GATEWAY);
        }
    }
}
