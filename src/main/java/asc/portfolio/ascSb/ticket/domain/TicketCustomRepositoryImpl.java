package asc.portfolio.ascSb.ticket.domain;

import asc.portfolio.ascSb.domain.ticket.QTicket;
import asc.portfolio.ascSb.ticket.dto.TicketForUserResponseDto;
import asc.portfolio.ascSb.cafe.domain.Cafe;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static asc.portfolio.ascSb.cafe.domain.QCafe.*;
import static asc.portfolio.ascSb.domain.ticket.QTicket.ticket;
import static asc.portfolio.ascSb.domain.user.QUser.user;

@Repository
@RequiredArgsConstructor
@Slf4j
public class TicketCustomRepositoryImpl implements TicketCustomRepository {

    private final JPAQueryFactory query;

    @Override
    public Optional<TicketForUserResponseDto> findAvailableTicketInfoByIdAndCafeName(Long id, String cafeName) {
        Ticket findTicket = query
                .selectFrom(ticket)
                .join(ticket.cafe, cafe).on(ticket.cafe.cafeName.eq(cafeName))
                .join(ticket.user, user).on(ticket.user.id.eq(id))
                .where(ticket.isValidTicket.eq(TicketStateType.VALID))
                .fetchOne();

        if (findTicket == null) {
            return Optional.empty();
        }

        if (findTicket.isValidFixedTermTicket() || findTicket.isValidPartTimeTicket()) {
            return findTicket.toTicketResponseDto();
        } else {
            log.error("Throw IllegalStateException. Ticket={}", findTicket);
            throw new IllegalStateException("Invalid Ticket");
        }
    }

    @Override
    public Optional<Ticket> findAvailableTicketByIdAndCafe(Long id, String cafeName) {
        Optional<Ticket> ticketOpt = Optional.ofNullable(query
                .selectFrom(ticket)
                .where(ticket.cafe.cafeName.eq(cafeName), ticket.user.id.eq(id), ticket.isValidTicket.eq(TicketStateType.VALID))
                .fetchOne());

        if (ticketOpt.isPresent()) {
            Ticket findTicket = ticketOpt.get();
            if (findTicket.isFixedTermTicket()) {
                if (!findTicket.isValidFixedTermTicket()) {
                    return Optional.empty();
                }
            } else {
                if (!findTicket.isValidPartTimeTicket()) {
                    return Optional.empty();
                }
            }
        }
        return ticketOpt;
    }

    @Override
    public Ticket findValidTicketInfoForAdminByUserIdAndCafeName(Long id, String cafeName) throws NullPointerException {
        LocalDateTime now = LocalDateTime.now();
        QTicket qTicket = new QTicket("qT");
        QTicket qTicketTwo = new QTicket("qTW");

        try {
            Optional<Ticket> checkRemainTimeIsNotNull = Optional.ofNullable(query
                    .select(qTicket)
                    .from(qTicket)
                    .leftJoin(qTicket.user, user)
                    .leftJoin(qTicket.cafe, cafe)
                    .where(user.id.eq(id), cafe.cafeName.eq(cafeName),
                            qTicket.remainingTime.isNotNull(),
                            qTicket.remainingTime.gt(0)
                    )
                    .fetchOne()); // 유효한 시간제 티켓이 있는지 확인
            Optional<Ticket> checkFixedTermIsNotNull = Optional.ofNullable(query
                    .select(qTicketTwo)
                    .from(qTicketTwo)
                    .leftJoin(qTicketTwo.user, user)
                    .leftJoin(qTicketTwo.cafe, cafe)
                    .where(user.id.eq(id), cafe.cafeName.eq(cafeName),
                            qTicketTwo.fixedTermTicket.isNotNull(),
                            qTicketTwo.fixedTermTicket.gt(now)
                    )
                    .fetchOne()); // 유효한 기간제 티켓이 있는지 확인
            if(checkRemainTimeIsNotNull.isPresent()) {
                return checkRemainTimeIsNotNull.get(); // 시간제 티켓이 존재 => 시간제 티켓 return
            } else if (checkFixedTermIsNotNull.isPresent()) {
                return checkFixedTermIsNotNull.get(); // 기간제 티켓이 존재 = > 기간제 티켓 return
            }
        } catch (NullPointerException exception) {
            log.error("Valid 티켓이 존재하지 않습니다.");
        }
        return null;
    }

    public Long verifyTicket() {
        LocalDateTime date = LocalDateTime.now();
        return query
                .update(ticket)
                .set(ticket.isValidTicket, TicketStateType.INVALID)
                .where(ticket.fixedTermTicket.lt(date).or(ticket.remainingTime.loe(0L)))
                .execute();
    }

    @Override
    public List<TicketForUserResponseDto> findAllTicketInfoByLoginIdAndCafe(String loginId, Cafe cafeObject) {
        return query
                .select(Projections.bean(TicketForUserResponseDto.class,
                        ticket.isValidTicket, ticket.fixedTermTicket, ticket.partTimeTicket, ticket.remainingTime, ticket.productLabel))
                .from(ticket)
                .join(ticket.cafe, cafe).on(cafe.eq(cafeObject))
                .join(ticket.user, user).on(user.loginId.eq(loginId))
                .orderBy(ticket.isValidTicket.desc(), ticket.createDate.asc())
                .fetch();
    }

    @Override
    public Long updateAllValidTicketState() {
        return query
                .update(ticket)
                .set(ticket.isValidTicket, TicketStateType.INVALID)
                .where(ticket.isValidTicket.eq(TicketStateType.VALID),
                        ticket.productLabel.contains("FIXED-TERM"),
                        ticket.fixedTermTicket.before(LocalDateTime.now()))
                .execute();
    }

    @Override
    public List<Ticket> findAllByIsValidTicketContains(TicketStateType ticketStateType) {
        return query
                .select(ticket)
                .from(ticket)
                .where(ticket.isValidTicket.eq(ticketStateType))
                .fetch();
    }
}


