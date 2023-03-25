package asc.portfolio.ascSb.seat.domain;

import asc.portfolio.ascSb.cafe.domain.Cafe;
import asc.portfolio.ascSb.cafe.domain.CafeFinder;
import asc.portfolio.ascSb.cafe.domain.QCafe;
import asc.portfolio.ascSb.reservation.domain.QReservation;
import asc.portfolio.ascSb.reservation.domain.Reservation;
import asc.portfolio.ascSb.reservation.domain.ReservationRepository;
import asc.portfolio.ascSb.reservation.domain.ReservationStatus;
import asc.portfolio.ascSb.ticket.domain.QTicket;
import asc.portfolio.ascSb.ticket.domain.Ticket;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Repository
public class SeatCustomRepositoryImpl implements SeatCustomRepository {

    private final JPAQueryFactory query;
    // todo : 이게 여기 있으면 안된다. 빠르게 삭제 요망
    private final CafeFinder cafeFinder;

    private final ReservationRepository reservationRepository;

    @Override
    public int updateAllReservedSeatStateWithFixedTermTicket() {
        int count = 0;

        // Fixed-Term Ticket Update
        List<Seat> seatList = query
                .selectFrom(QSeat.seat)
                .join(QSeat.seat.ticket, QTicket.ticket)
                .where(QSeat.seat.isReserved.isTrue(),
                        QTicket.ticket.productLabel.contains("FIXED-TERM"),
                        QTicket.ticket.fixedTermTicket.before(LocalDateTime.now()))
                .fetch();

        // 만료된 Fixed-Term Ticket 에 따른 Seat, reservation 종료 처리
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
                .selectFrom(QSeat.seat)
                .join(QSeat.seat.ticket, QTicket.ticket)
                .where(QSeat.seat.isReserved.isTrue(),
                        QTicket.ticket.productLabel.contains("PART-TIME"))
                .fetch();

        for (Seat seatOne : seatList) {
            Cafe cafe = cafeFinder.findById(seatOne.getCafeId());
            Reservation info = query
                    .selectFrom(QReservation.reservation)
                    .where(QReservation.reservation.isValid.eq(ReservationStatus.VALID),
                            QReservation.reservation.cafeName.eq(cafe.getCafeName()),
                            QReservation.reservation.seatNumber.eq(seatOne.getSeatNumber()))
                    .fetchOne();

            if (info == null) {
                log.error("예약된 Seat에 유효한 reservation 없습니다. seat = {}, {}", cafe.getCafeName(), seatOne.getSeatNumber());
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
    public int updateAllReservedStatusWithStartTime() {
        int count = 0;
        List<Reservation> reservationList = query
                .selectFrom(QReservation.reservation)
                .where(QReservation.reservation.isValid.eq(ReservationStatus.VALID),
                        QReservation.reservation.endTime.before(LocalDateTime.now()))
                .fetch();

        for (Reservation info : reservationList) {
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
                .selectFrom(QSeat.seat)
                .join(QSeat.seat.ticket, QTicket.ticket)
                .where(QSeat.seat.isReserved.isTrue(),
                        QTicket.ticket.productLabel.contains("FIXED-TERM"),
                        QTicket.ticket.fixedTermTicket.after(timeAfter),
                        QTicket.ticket.fixedTermTicket.before(timeBefore))
                .fetch();
    }

    @Override
    public List<Seat> getAlmostFinishedSeatListWithStartTime(Long minute) {
        //after(10분전) ~ before(9분전) ~~~ 현재

        LocalDateTime timeNow = LocalDateTime.now();
        LocalDateTime timeAfter = timeNow.plusMinutes(minute);
        LocalDateTime timeBefore = timeNow.plusMinutes(minute + 1);

        List<Reservation> reservationList = query
                .selectFrom(QReservation.reservation)
                .where(QReservation.reservation.isValid.eq(ReservationStatus.VALID),
                        QReservation.reservation.endTime.after(timeAfter),
                        QReservation.reservation.endTime.before(timeBefore))
                .fetch();


        List<Seat> seatList = new ArrayList<>();
        for (Reservation info : reservationList) {
            Seat findSeat = findByCafeNameAndSeatNumber(info.getCafeName(), info.getSeatNumber());
            seatList.add(findSeat);
        }

        return seatList;
    }

    private void exitSeatBySeatEntity(Seat seatOne, Reservation reservation) {
        Cafe cafe = cafeFinder.findById(seatOne.getCafeId());
        String cafeName = cafe.getCafeName();

        if (reservation == null) {
            reservation = reservationRepository.findValidReservationByCafeNameAndSeatNumber(cafeName, seatOne.getSeatNumber());
        }

        Ticket ticketOne = seatOne.getTicket();

        log.debug("Seat 사용 종료. cafeName={}, seatNumber={}", cafeName, seatOne.getSeatNumber());
        ticketOne.exitUsingTicket(reservation.updateTimeInUse());
        seatOne.exitSeat();
        reservation.endUsingSeat();
    }

    @Override
    public Seat findByCafeAndSeatNumber(Cafe cafeObject, Integer seatNumber) {
        return query
                .selectFrom(QSeat.seat)
                .where(QSeat.seat.cafeId.eq(cafeObject.getId()), QSeat.seat.seatNumber.eq(seatNumber))
                .fetchOne();
    }

    @Override
    public Seat findByCafeNameAndSeatNumber(String cafeName, int seatNumber) {
        return query
                .select(QSeat.seat)
                .from(QSeat.seat)
                .join(QSeat.seat).on(QCafe.cafe.id.eq(QSeat.seat.cafeId))
                .where(QCafe.cafe.cafeName.eq(cafeName), QSeat.seat.seatNumber.eq(seatNumber))
                .fetchOne();
    }
}
