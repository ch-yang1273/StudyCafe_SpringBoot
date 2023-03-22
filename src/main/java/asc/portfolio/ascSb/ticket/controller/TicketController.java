package asc.portfolio.ascSb.ticket.controller;

import asc.portfolio.ascSb.common.auth.LoginUser;
import asc.portfolio.ascSb.ticket.service.TicketService;
import asc.portfolio.ascSb.ticket.dto.TicketForAdminResponseDto;
import asc.portfolio.ascSb.ticket.dto.TicketForUserResponseDto;
import asc.portfolio.ascSb.user.service.UserRoleCheckService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/ticket")
public class TicketController {

    private final TicketService ticketService;
    private final UserRoleCheckService userRoleCheckService;

    @GetMapping("/{cafeName}")
    public TicketForUserResponseDto userTicket(@LoginUser Long userId, @PathVariable String cafeName) {
        return ticketService.userValidTicket(userId, cafeName);
    }

    @GetMapping("/lookup")
    public ResponseEntity<List<TicketForUserResponseDto>> lookupUserTickets(@LoginUser Long adminId,
                                                                            @RequestParam("user") String targetUserLoginId) {

        userRoleCheckService.isAdmin(adminId);
        log.info("lookup tickets. user = {}", targetUserLoginId);
        List<TicketForUserResponseDto> ticketForUserResponseDtos = ticketService.lookupUserTickets(targetUserLoginId, adminId);
        return new ResponseEntity<>(ticketForUserResponseDtos, HttpStatus.OK);
    }

    @GetMapping("/admin/lookup")
    public ResponseEntity<TicketForAdminResponseDto> adminLookUpUserValidTicket(@LoginUser Long adminId, @RequestParam String userLoginId) {
        userRoleCheckService.isAdmin(adminId);
        return new ResponseEntity<>(ticketService.adminLookUpUserValidTicket(userLoginId, adminId), HttpStatus.OK);
    }
}
