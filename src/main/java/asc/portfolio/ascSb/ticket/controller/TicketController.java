package asc.portfolio.ascSb.ticket.controller;

import asc.portfolio.ascSb.user.domain.User;
import asc.portfolio.ascSb.user.domain.UserRoleType;
import asc.portfolio.ascSb.common.auth.LoginUser;
import asc.portfolio.ascSb.ticket.service.TicketService;
import asc.portfolio.ascSb.ticket.dto.TicketForAdminResponseDto;
import asc.portfolio.ascSb.ticket.dto.TicketForUserResponseDto;
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

    @GetMapping("/{cafeName}")
    public TicketForUserResponseDto userTicket(@LoginUser User user, @PathVariable String cafeName) {
        return ticketService.userValidTicket(user.getId(), cafeName);
    }

    @GetMapping("/lookup")
    public ResponseEntity<List<TicketForUserResponseDto>> lookupUserTickets(@LoginUser User admin,
                                                                            @RequestParam("user") String targetUserLoginId) {
        if (admin.getRole() == UserRoleType.ADMIN) {
            if (admin.getCafe() != null) {
                log.info("lookup tickets. user = {}", targetUserLoginId);
                List<TicketForUserResponseDto> ticketForUserResponseDtos = ticketService.lookupUserTickets(targetUserLoginId, admin.getCafe());
                return new ResponseEntity<>(ticketForUserResponseDtos, HttpStatus.OK);
            } else {
                log.error("Set up a cafe field first");
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            }

        } else {
            log.info("Unauthorized User");
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/admin/lookup")
    public ResponseEntity<TicketForAdminResponseDto> adminLookUpUserValidTicket(@LoginUser User user, @RequestParam String userLoginId) {
        if(user.getRole() != UserRoleType.ADMIN) {
            log.error("관리자 계정이 아닙니다.");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (userLoginId != null) {
            return new ResponseEntity<>(ticketService.adminLookUpUserValidTicket(userLoginId, user.getCafe().getCafeName()), HttpStatus.OK);
        } else {
            log.error("유효한 티켓이 없습니다.");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
