package asc.portfolio.ascSb.bootpay.controller;

import asc.portfolio.ascSb.order.domain.Orders;
import asc.portfolio.ascSb.common.auth.LoginUser;
import asc.portfolio.ascSb.bootpay.service.payment.PaymentService;
import asc.portfolio.ascSb.order.service.OrderService;
import asc.portfolio.ascSb.bootpay.domain.Bootpay;
import asc.portfolio.ascSb.bootpay.dto.BootPayOrderDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.bootpay.model.request.Cancel;
import kr.co.bootpay.model.response.ResDefault;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/pay")
public class BootPayController {
    private final OrderService orderService;
    private final PaymentService paymentService;

    @GetMapping("/confirm")
    public ResponseEntity<?> confirmPay(
            @LoginUser Long userId,
            @RequestParam("receipt-id") String receipt_id) throws Exception {

        String getDataJson = "";
        BootPayOrderDto dto = null;
        String rest_application_id = "";
        String private_key = "";

        Bootpay api = new Bootpay(
                rest_application_id,
                private_key
        );

        try {
            ResDefault<HashMap<String, Object>> token = api.getAccessToken(); // 부트페이 서버에서 토큰 검증
            ResDefault<HashMap<String, Object>> check = api.verify(receipt_id);
            getDataJson = check.toJson();
            ObjectMapper objectMapper = new ObjectMapper();
            dto = objectMapper.readValue(getDataJson, BootPayOrderDto.class);
            log.info("결제 토큰 검증완료 data = {}", check.data);
        } catch (Exception e) {
            e.printStackTrace();
        }

        String orderReceiptId = dto.getData().getReceipt_id();
        Orders orders = orderService.findOrderByReceiptId(orderReceiptId); // ReceiptId 에 맞는 Orders를 검색
        log.info("findOrders완료");
        int price = Math.toIntExact(orders.getOrderPrice());

        // 검증된 가격이 Orders에 저장 가격과 일치하는지(변조방지), 결제 승인 대기 상태가 맞는지, status가 200인지 확인
        if (dto.getStatus() == 200 && dto.getData().getPrice() == price && dto.getData().getStatus() == 2) {
            log.info("BootPay 서버 <-> 제품 검증 완료");

            /* 검증 완료시 orders 상태 Done(완료)으로 변경, Product에 제품추가, Ticket에 이용권추가 */
            boolean isCompleted = paymentService.modifyAndAddValidPayment(orders, userId, dto);
            if(isCompleted) {
                return ResponseEntity.ok("OK");
            }
        }

        //서버 검증 오류시
        Cancel cancel = new Cancel();
        cancel.receiptId = receipt_id;
        cancel.name = "관리자";
        cancel.reason = "서버 검증 오류";
        orders.failOrder();

        //결제 오류 로그
        String cancelDataJson = "";
        try {
            ResDefault<HashMap<String, Object>> cancelRes = api.receiptCancel(cancel);
            cancelDataJson = cancelRes.toJson();
            log.info("결제실패 Log {}", cancelDataJson);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.badRequest().body("FAIL"); // 환불
    }
}
