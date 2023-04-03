package asc.portfolio.ascSb.ticket.controller;

import asc.portfolio.ascSb.common.auth.LoginUser;
import asc.portfolio.ascSb.ticket.service.TicketService;
import asc.portfolio.ascSb.ticket.dto.TicketForAdminResponse;
import asc.portfolio.ascSb.ticket.dto.TicketStatusResponse;
import asc.portfolio.ascSb.user.service.UserRoleCheckService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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
    public TicketStatusResponse userTicket(@LoginUser Long userId, @PathVariable String cafeName) {
        return ticketService.userValidTicket(userId, cafeName);
    }

    @GetMapping("/lookup")
    public ResponseEntity<List<TicketStatusResponse>> lookupUserTickets(@LoginUser Long adminId,
                                                                        @RequestParam("user") String targetUserLoginId) {

        userRoleCheckService.isAdmin(adminId);
        List<TicketStatusResponse> ticketStatusResponse = ticketService.lookupUserTickets(targetUserLoginId, adminId);
        return new ResponseEntity<>(ticketStatusResponse, HttpStatus.OK);
    }

    @GetMapping("/admin/lookup")
    public ResponseEntity<TicketForAdminResponse> adminLookUpUserValidTicket(@LoginUser Long adminId, @RequestParam String userLoginId) {
        userRoleCheckService.isAdmin(adminId);
        return new ResponseEntity<>(ticketService.adminLookUpUserValidTicket(userLoginId, adminId), HttpStatus.OK);
    }
}
