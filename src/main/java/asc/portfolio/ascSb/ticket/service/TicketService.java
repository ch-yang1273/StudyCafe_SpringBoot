package asc.portfolio.ascSb.ticket.service;

import asc.portfolio.ascSb.cafe.domain.Cafe;
import asc.portfolio.ascSb.order.domain.Orders;
import asc.portfolio.ascSb.ticket.domain.Ticket;
import asc.portfolio.ascSb.user.domain.User;
import asc.portfolio.ascSb.common.infra.bootpay.dto.BootPayOrderDto;
import asc.portfolio.ascSb.ticket.dto.TicketForAdminResponseDto;
import asc.portfolio.ascSb.ticket.dto.TicketForUserResponseDto;

import java.util.List;

public interface TicketService {

    TicketForUserResponseDto userValidTicket(Long id, String cafeName);

    Long saveProductToTicket(Long userId, BootPayOrderDto bootPayOrderDto, Orders orders);

    public List<TicketForUserResponseDto> lookupUserTickets(String targetUserLoginId, Long adminId);
//
//    Long saveTicket(TicketRequestDto dto, Long id);

    TicketForAdminResponseDto adminLookUpUserValidTicket(String userLoginID, Long adminId);

    void setInvalidTicket(String productLabel);

    public Long updateAllValidTicketState();

    List<Ticket> allInvalidTicketInfo();

    void deleteInvalidTicket(List<Ticket> tickets);


}
