package asc.portfolio.ascSb.order.controller;

import asc.portfolio.ascSb.common.auth.LoginUser;
import asc.portfolio.ascSb.order.dto.OrderRequest;
import asc.portfolio.ascSb.order.dto.OrderResponse;
import asc.portfolio.ascSb.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/order")
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<Void> createOrder(@LoginUser Long userId, @RequestBody OrderRequest dto) {
        orderService.saveOrder(userId, dto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("{customer}")
    public ResponseEntity<List<OrderResponse>> getOrders(@LoginUser Long userId, @PathVariable String customer) {
        return ResponseEntity.ok().body(orderService.findOrders(userId, customer));
    }

    @PostMapping("/confirm")
    public ResponseEntity<Void> confirmPayment(@LoginUser Long userId, @RequestParam("receipt-id") String receipt_id) {
        orderService.confirmPayment(userId, receipt_id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/cancel")
    public ResponseEntity<Void> cancelPayment(@LoginUser Long adminId, @RequestParam("id") Long orderId) {
        orderService.cancelPayment(adminId, orderId);
        return ResponseEntity.ok().build();
    }
}
