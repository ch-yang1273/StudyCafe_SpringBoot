package asc.portfolio.ascSb.seat.domain;
import asc.portfolio.ascSb.cafe.domain.Cafe;
import asc.portfolio.ascSb.seatreservationinfo.domain.SeatReservationInfo;
import asc.portfolio.ascSb.seatreservationinfo.domain.SeatReservationInfoRepository;
import asc.portfolio.ascSb.seatreservationinfo.domain.SeatReservationInfoStateType;
import asc.portfolio.ascSb.ticket.domain.Ticket;
import asc.portfolio.ascSb.seat.dto.SeatSelectResponseDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static asc.portfolio.ascSb.cafe.domain.QCafe.*;
import static asc.portfolio.ascSb.domain.seat.QSeat.*;
import static asc.portfolio.ascSb.domain.seatreservationinfo.QSeatReservationInfo.seatReservationInfo;
import static asc.portfolio.ascSb.domain.ticket.QTicket.*;

@Slf4j
@RequiredArgsConstructor
@Repository
public class SeatCustomRepositoryImpl implements SeatCustomRepository {

    private final JPAQueryFactory query;

    private final SeatReservationInfoRepository seatReservationInfoRepository;

    @Override
    public int updateAllReservedSeatStateWithFixedTermTicket() {
        int count = 0;

        // Fixed-Term Ticket Update
        List<Seat> seatList = query
                .selectFrom(seat)
                .join(seat.ticket, ticket)
                .where(seat.seatState.eq(SeatStateType.RESERVED),
                        ticket.productLabel.contains("FIXED-TERM"),
                        ticket.fixedTermTicket.before(LocalDateTime.now()))
                .fetch();

        // 만료된 Fixed-Term Ticket 에 따른 Seat, seatReservationInfo 종료 처리
        for (Seat seatOne : seatList) {
            count++;
            log.debug("Exited by FixedTermTicket update");
            exitSeatBySeatEntity(seatOne, null);
        }

        return count;
    }

    @Override
    public int updateAllReservedSeatStateWithPartTimeTicket() {
        int count = 0;

        // Part-Time Ticket Update
        List<Seat> seatList = query
                .selectFrom(seat)
                .join(seat.ticket, ticket)
                .where(seat.seatState.eq(SeatStateType.RESERVED),
                        ticket.productLabel.contains("PART-TIME"))
                .fetch();

        for (Seat seatOne : seatList) {
            Cafe cafe = seatOne.getCafe();
            SeatReservationInfo info = query
                    .selectFrom(seatReservationInfo)
                    .where(seatReservationInfo.isValid.eq(SeatReservationInfoStateType.VALID),
                            seatReservationInfo.cafeName.eq(cafe.getCafeName()),
                            seatReservationInfo.seatNumber.eq(seatOne.getSeatNumber()))
                    .fetchOne();

            if (info == null) {
                log.error("예약된 Seat에 유효한 SeatReservationInfo가 없습니다. seat = {}, {}", cafe.getCafeName(), seatOne.getSeatNumber());
                return 0;
            }

            Ticket ticketOne = info.getTicket();
            Long remainTime = ticketOne.getRemainingTime();
            Long timeInUse = info.updateTimeInUse();

            if (timeInUse >= remainTime) {
                count++;
                log.debug("Exited by PartTimeTicket update");
                exitSeatBySeatEntity(seatOne, info);
            }
        }

        return count;
    }

    @Override
    public int updateAllReservedSeatStateWithStartTime() {
        int count = 0;
        List<SeatReservationInfo> seatRezInfoList = query
                .selectFrom(seatReservationInfo)
                .where(seatReservationInfo.isValid.eq(SeatReservationInfoStateType.VALID),
                        seatReservationInfo.endTime.before(LocalDateTime.now()))
                .fetch();

        for (SeatReservationInfo info : seatRezInfoList) {
            count++;
            log.debug("Exited by StartTime update, endTime={}", info.getEndTime());
            Seat findSeat = findByCafeNameAndSeatNumber(info.getCafeName(), info.getSeatNumber());
            exitSeatBySeatEntity(findSeat, info);
        }

        return count;
    }

    @Override
    public List<Seat> getAlmostFinishedSeatListWithFixedTermTicket(Long minute) {
        //after(10분전) ~ before(9분전) ~~~ 현재

        LocalDateTime timeNow = LocalDateTime.now();
        LocalDateTime timeAfter = timeNow.plusMinutes(minute);
        LocalDateTime timeBefore = timeNow.plusMinutes(minute + 1);

        return query
                .selectFrom(seat)
                .join(seat.ticket, ticket)
                .where(seat.seatState.eq(SeatStateType.RESERVED),
                        ticket.productLabel.contains("FIXED-TERM"),
                        ticket.fixedTermTicket.after(timeAfter),
                        ticket.fixedTermTicket.before(timeBefore))
                .fetch();
    }

    @Override
    public List<Seat> getAlmostFinishedSeatListWithStartTime(Long minute) {
        //after(10분전) ~ before(9분전) ~~~ 현재

        LocalDateTime timeNow = LocalDateTime.now();
        LocalDateTime timeAfter = timeNow.plusMinutes(minute);
        LocalDateTime timeBefore = timeNow.plusMinutes(minute + 1);

        List<SeatReservationInfo> seatRezInfoList = query
                .selectFrom(seatReservationInfo)
                .where(seatReservationInfo.isValid.eq(SeatReservationInfoStateType.VALID),
                        seatReservationInfo.endTime.after(timeAfter),
                        seatReservationInfo.endTime.before(timeBefore))
                .fetch();


        List<Seat> seatList = new ArrayList<>();
        for (SeatReservationInfo info : seatRezInfoList) {
            Seat findSeat = findByCafeNameAndSeatNumber(info.getCafeName(), info.getSeatNumber());
            seatList.add(findSeat);
        }

        return seatList;
    }

    private void exitSeatBySeatEntity(Seat seatOne, SeatReservationInfo seatRezInfoEntity) {
        String cafeName = seatOne.getCafe().getCafeName();

        if (seatRezInfoEntity == null) {
            seatRezInfoEntity = seatReservationInfoRepository.findValidSeatRezInfoByCafeNameAndSeatNumber(cafeName, seatOne.getSeatNumber());
        }

        Ticket ticketOne = seatOne.getTicket();

        log.debug("Seat 사용 종료. cafeName={}, seatNumber={}", cafeName, seatOne.getSeatNumber());
        ticketOne.exitUsingTicket(seatRezInfoEntity.updateTimeInUse());
        seatOne.exitSeat();
        seatRezInfoEntity.endUsingSeat();
    }

//    public void updateSeatState(String cafeName) {
//        List<Seat> seatList = query
//                .selectFrom(seat)
//                .where(seat.cafe.cafeName.eq(cafeName))
//                .orderBy(seat.seatNumber.asc())
//                .fetch();
//
//        for (Seat seat : seatList) {
//            if (seat.getSeatState() == SeatStateType.RESERVED) {
//                if (!seat.getTicket().isValidTicket()) {
//                    log.info("Update SeatState SeatNumber={}", seat.getSeatNumber());
//                    seat.setSeatStateTypeUnReserved();
//                }
//            }
//        }
//    }

    public List<SeatSelectResponseDto> findSeatNumberAndSeatStateList(String cafeName) {
        //updateSeatState(cafeName);

        return query
                .select(Projections.bean(SeatSelectResponseDto.class, seat.seatNumber, seat.seatState))
                .from(seat)
                .where(seat.cafe.cafeName.eq(cafeName))
                .orderBy(seat.seatNumber.asc())
                .fetch();
    }

    @Override
    public Seat findByCafeAndSeatNumber(Cafe cafeObject, Integer seatNumber) {
        return query
                .select(seat)
                .from(seat)
                .join(seat.cafe, cafe)
                .where(cafe.eq(cafeObject), seat.seatNumber.eq(seatNumber))
                .fetchOne();
    }

    @Override
    public Seat findByCafeNameAndSeatNumber(String cafeName, int seatNumber) {
        return query
                .select(seat)
                .from(seat)
                .join(seat.cafe, cafe)
                .where(cafe.cafeName.eq(cafeName), seat.seatNumber.eq(seatNumber))
                .fetchOne();
    }
}
