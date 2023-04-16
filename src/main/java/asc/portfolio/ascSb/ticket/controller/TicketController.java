package asc.portfolio.ascSb.ticket.controller;

import asc.portfolio.ascSb.common.auth.LoginUser;
import asc.portfolio.ascSb.ticket.service.TicketService;
import asc.portfolio.ascSb.ticket.dto.TicketStatusResponse;
import asc.portfolio.ascSb.user.service.UserRoleCheckService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/ticket")
public class TicketController {

    private final TicketService ticketService;
    private final UserRoleCheckService userRoleCheckService;

    @GetMapping("/{cafeName}")
    public ResponseEntity<TicketStatusResponse> getMyTicket(@LoginUser Long userId, @PathVariable String cafeName) {
        // todo : 소유한 모든 카페의 Ticket을 가져오도록 수정
        return ResponseEntity.ok().body(ticketService.userValidTicket(userId, cafeName));
    }

    @GetMapping("/lookup")
    public ResponseEntity<List<TicketStatusResponse>> lookupUserTickets(@LoginUser Long adminId,
                                                                        @RequestParam("user") String targetUserLoginId) {
        // todo : 조건 검색 추가
        return ResponseEntity.ok().body(ticketService.lookupUserTickets(targetUserLoginId, adminId));
    }
}
