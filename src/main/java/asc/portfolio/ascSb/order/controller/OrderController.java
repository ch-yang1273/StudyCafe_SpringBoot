package asc.portfolio.ascSb.order.controller;

import asc.portfolio.ascSb.bootpay.dto.BootPayReceipt;
import asc.portfolio.ascSb.bootpay.infra.BootPayApiService;
import asc.portfolio.ascSb.common.auth.LoginUser;
import asc.portfolio.ascSb.order.domain.Orders;
import asc.portfolio.ascSb.order.dto.OrderRequest;
import asc.portfolio.ascSb.order.service.OrderService;
import asc.portfolio.ascSb.product.service.ProductService;
import asc.portfolio.ascSb.ticket.service.TicketService;
import asc.portfolio.ascSb.user.service.UserRoleCheckService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/order")
public class OrderController {

    private final OrderService orderService;
    private final BootPayApiService bootPayApiService;
    private final UserRoleCheckService userRoleCheckService;

    //todo 삭제
    private final ProductService productService;
    private final TicketService ticketService;

    @PostMapping
    public ResponseEntity<Void> order(@LoginUser Long userId, @RequestBody OrderRequest dto) {
        orderService.saveOrder(userId, dto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/confirm")
    public ResponseEntity<Void> confirmPay(
            @LoginUser Long userId,
            @RequestParam("receipt-id") String receipt_id) {

        BootPayReceipt dto = bootPayApiService.getReceipt(receipt_id);

        String receiptId = dto.getReceipt_id();

        Orders orders = orderService.findOrderByReceiptId(receiptId); // ReceiptId 에 맞는 Orders를 검색
        int price = orders.getPrice();

        boolean isValid = bootPayApiService.crossValidation(dto, 1, price);
        if (isValid) {
            BootPayReceipt confirm = bootPayApiService.confirm(receiptId);
            log.info("Cancel receiptId={}, at={}", receiptId, confirm.getPurchased_at());
            orders.completeOrder();

            /* 검증 완료시 orders 상태 Done(완료)으로 변경, Product에 제품추가, Ticket에 이용권추가 */
            productService.saveProduct(userId, orders);
            ticketService.saveProductToTicket(userId, orders);
            return ResponseEntity.ok().build();
        }

        //서버 검증 오류시
        orders.failOrder();
        BootPayReceipt resp = bootPayApiService.cancelReceipt(receiptId);
        log.info("Cancel receiptId={}, at={}", receiptId, resp.getCancelled_at());
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/cancel")
    public ResponseEntity<Void> cancelPay(@LoginUser Long userId, @RequestParam("id") Long productId) {
        userRoleCheckService.isAdmin(userId);

        final String receiptId = orderService.findReceiptIdToProductLabel(productId).getReceiptId();
        BootPayReceipt receipt = bootPayApiService.getReceipt(receiptId);

        BootPayReceipt resp = bootPayApiService.cancelReceipt(receipt.getReceipt_id());
        productService.cancelProduct(productId);
        ticketService.setInvalidTicket(productId);

        return ResponseEntity.ok().build();
    }
}
