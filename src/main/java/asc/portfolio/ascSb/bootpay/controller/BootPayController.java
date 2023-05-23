package asc.portfolio.ascSb.bootpay.controller;

import asc.portfolio.ascSb.bootpay.dto.BootPayReceipt;
import asc.portfolio.ascSb.bootpay.exception.BootPayErrorData;
import asc.portfolio.ascSb.bootpay.exception.BootPayException;
import asc.portfolio.ascSb.bootpay.infra.BootPayTokenService;
import asc.portfolio.ascSb.common.auth.LoginUser;
import asc.portfolio.ascSb.order.domain.Orders;
import asc.portfolio.ascSb.order.service.OrderService;
import asc.portfolio.ascSb.product.service.ProductService;
import asc.portfolio.ascSb.ticket.service.TicketService;
import asc.portfolio.ascSb.user.service.UserRoleCheckService;
import kr.co.bootpay.Bootpay;
import kr.co.bootpay.model.request.Cancel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/pay")
public class BootPayController {

    private final OrderService orderService;
    private final BootPayTokenService bootPayTokenService;
    private final UserRoleCheckService userRoleCheckService;

    // todo 삭제
    private final ProductService productService;
    private final TicketService ticketService;

    private void refreshAccessToken(Bootpay api) {
        HashMap<String, Object> res;
        try {
            res = api.getAccessToken();
        } catch (Exception e) {
            throw new BootPayException(BootPayErrorData.TOKEN_REFRESH_ERROR);
        }

        if(res.get("error_code") == null) { //success
            log.info("goGetToken success: {}", res);
        } else {
            log.error("goGetToken false: {}", res);
            throw new BootPayException(BootPayErrorData.TOKEN_REFRESH_ERROR);
        }
    }

    private BootPayReceipt getReceipt(Bootpay api, String receiptId) {
        // 매번 AccessToken을 갱신해야 한다.
        refreshAccessToken(api);

        BootPayReceipt dto;
        try {
            HashMap<String, Object> receipt = api.getReceipt(receiptId);
            dto = BootPayReceipt.of(receipt);
        } catch (Exception e) {
            throw new BootPayException(BootPayErrorData.RECEIPT_LOOKUP_ERROR);
        }

        return dto;
    }

    /**
     * 교차 검증 대상 : status, price
     * 결제 완료에 대한 교차검증을 하려면 결제 단건 조회를 통해 status 값이 1인지 대조합니다.
     * 실제 요청했던 결제금액과 결제 단건 조회를 통해 리턴된 price 값이 일치하는지 대조합니다.
      */
    private boolean crossValidation(BootPayReceipt dto, int status, int price) {
        return (dto.getStatus() == status && dto.getPrice() == price);
    }

    private BootPayReceipt confirm(Bootpay api, String receiptId) {
        try {
            HashMap<String, Object> resp = api.confirm(receiptId);
            return BootPayReceipt.of(resp);
        } catch (Exception e) {
            throw new BootPayException(BootPayErrorData.RECEIPT_CONFIRM_ERROR);
        }
    }

    private BootPayReceipt cancelReceipt(Bootpay api, String receiptId) {
        Cancel cancel = new Cancel();
        cancel.receiptId = receiptId;
        cancel.cancelUsername = "관리자";
        cancel.cancelMessage = "서버 검증 오류";

        try {
            return BootPayReceipt.of(api.receiptCancel(cancel));
        } catch (Exception e) {
            throw new BootPayException(BootPayErrorData.RECEIPT_CANCEL_ERROR);
        }
    }

    @GetMapping("/confirm")
    public ResponseEntity<Void> confirmPay(
            @LoginUser Long userId,
            @RequestParam("receipt-id") String receipt_id) {

        Bootpay api = bootPayTokenService.getBootPayApi();
        BootPayReceipt dto = getReceipt(api, receipt_id);

        String receiptId = dto.getReceipt_id();

        // todo : 수정 필요, Orders 만들 때부터 Cafe 정보가 있어야 함.
        Orders orders = orderService.findOrderByReceiptId(receiptId); // ReceiptId 에 맞는 Orders를 검색
        int price = Math.toIntExact(orders.getPrice());

        boolean isValid = crossValidation(dto, 1, price);
        if (isValid) {
            BootPayReceipt confirm = confirm(api, receiptId);
            log.info("Cancel receiptId={}, at={}", receiptId, confirm.getPurchased_at());
            orders.completeOrder();

            /* 검증 완료시 orders 상태 Done(완료)으로 변경, Product에 제품추가, Ticket에 이용권추가 */
            productService.saveProduct(userId, orders);
            ticketService.saveProductToTicket(userId, orders);
            return ResponseEntity.ok().build();
        }

        //서버 검증 오류시
        orders.failOrder();
        BootPayReceipt resp = cancelReceipt(api, receiptId);
        log.info("Cancel receiptId={}, at={}", receiptId, resp.getCancelled_at());
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/cancel")
    public ResponseEntity<Void> cancelPay(@LoginUser Long userId, @RequestParam("id") Long productId) {

        userRoleCheckService.isAdmin(userId);
        Bootpay api = bootPayTokenService.getBootPayApi();

        final String receiptId = orderService.findReceiptIdToProductLabel(productId).getReceiptId();
        BootPayReceipt receipt = getReceipt(api, receiptId);

        BootPayReceipt resp = cancelReceipt(api, receipt.getReceipt_id());
        productService.cancelProduct(productId);
        ticketService.setInvalidTicket(productId);

        return ResponseEntity.ok().build();
    }
}
