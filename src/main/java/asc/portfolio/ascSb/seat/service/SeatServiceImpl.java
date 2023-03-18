package asc.portfolio.ascSb.seat.service;
import asc.portfolio.ascSb.cafe.domain.Cafe;
import asc.portfolio.ascSb.common.infra.redis.RedisRepository;
import asc.portfolio.ascSb.seat.domain.Seat;
import asc.portfolio.ascSb.seat.domain.SeatRepository;
import asc.portfolio.ascSb.seatreservationinfo.domain.SeatReservationInfo;
import asc.portfolio.ascSb.seatreservationinfo.domain.SeatReservationInfoRepository;
import asc.portfolio.ascSb.ticket.domain.Ticket;
import asc.portfolio.ascSb.ticket.domain.TicketRepository;
import asc.portfolio.ascSb.user.domain.User;
import asc.portfolio.ascSb.firebase.service.FirebaseCloudMessageService;
import asc.portfolio.ascSb.seat.dto.SeatResponseDto;
import asc.portfolio.ascSb.user.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class SeatServiceImpl implements SeatService {

    private final SeatRepository seatRepository;

    private final UserRepository userRepository;

    private final SeatReservationInfoRepository reservationInfoRepository;

    private final TicketRepository ticketRepository;

    private final RedisRepository redisRepository;

    private final FirebaseCloudMessageService firebaseCloudMessageService;

    @Override
    public SeatResponseDto showSeatStateOne(Long userId, Integer seatNumber) {
        User user = userRepository.findById(userId).orElseThrow();
        Cafe cafe = user.getCafe();

        if ((cafe.getCafeName() == null) || (seatNumber == null)) {
            log.info("NullPointer Ex : cafeName, seatNumber");
            throw new NullPointerException("NullPointer Ex : cafeName, seatNumber");
        }

        Seat seat = seatRepository.findByCafeAndSeatNumber(cafe, seatNumber);
        if (!seat.isReserved()) {
            return SeatResponseDto.createUnReservedSeatDto(seatNumber);
        }

        Ticket ticket = seat.getTicket();
        SeatReservationInfo rezInfo = reservationInfoRepository.findValidSeatRezInfoByCafeNameAndSeatNumber(cafe.getCafeName(),
                seat.getSeatNumber());

        if (ticket.isValidFixedTermTicket()) {
            return SeatResponseDto.setFixedTermSeat(
                    seatNumber,
                    rezInfo.getStartTime(),
                    rezInfo.updateTimeInUse(),
                    ticket.getFixedTermTicket());
        } else if (ticket.isValidPartTimeTicket()) {
            return SeatResponseDto.setPartTimeSeat(
                    seatNumber,
                    rezInfo.getStartTime(),
                    rezInfo.updateTimeInUse(),
                    ticket.getPartTimeTicket(),
                    ticket.getRemainingTime());
        } else {
            log.error("IllegalState Ex. Ticket id ={}", ticket.getId());
            throw new IllegalStateException("IllegalState Ex. Ticket id =" + ticket.getId());
        }
    }

    @Override
    public Boolean exitSeat(Long userId) {
        User user = userRepository.findById(userId).orElseThrow();

        //ReservationInfo 수정
        List<SeatReservationInfo> userRezInfo = reservationInfoRepository.findValidSeatRezInfoByLoginId(user.getLoginId());
        if (userRezInfo != null) {
            if (userRezInfo.size() > 1) {
                log.error("Valid SeatReservationInfo is Over One");
            }

            for (SeatReservationInfo info : userRezInfo) {
                //Reservation Info Exit
                Long timeInUse = info.endUsingSeat();

                //Seat Exit
                Seat findSeat = seatRepository.findByCafeNameAndSeatNumber(info.getCafeName(), info.getSeatNumber());
                findSeat.exitSeat();

                //Ticket Exit
                Ticket ticket = findSeat.getTicket();
                if (ticket == null) {
                    log.error("ticket == null");
                } else if (ticket.isFixedTermTicket()) {
                    ticket.exitUsingTicket(null);
                } else {
                    ticket.exitUsingTicket(timeInUse); // 사용한 시간 startTime or timeInUse
                }

            }
        }

        // seat Table 의 User_ID Unique 를 유지하기 위해, 먼저 DB에 반영
        reservationInfoRepository.flush();
        seatRepository.flush();
        ticketRepository.flush();

        return true;
    }

    @Override
    public void exitSeatBySeatNumber(Long adminId, int seatNumber) {
        User admin = userRepository.findById(adminId).orElseThrow();
        Cafe cafe = admin.getCafe();
        Seat findSeat = seatRepository.findByCafeAndSeatNumber(cafe, seatNumber);

        //todo exitMySeat, exitSeatBySeatNumber로 구분해야겠다.
        exitSeat(findSeat.getUser().getId());
    }

    @Override
    public Boolean reserveSeat(Long userId, Integer seatNumber, Long startTime) {
        User user = userRepository.findById(userId).orElseThrow();
        Cafe cafe = user.getCafe();
        if (cafe == null) {
            log.error("선택 된 카페가 없는 유저 입니다.");
            return false;
        }

        if ((seatNumber == null) || (startTime == null)) {
            throw new NullPointerException("NullPointerException : seatNumber, startTime");
        }

        Seat findSeat = seatRepository.findByCafeAndSeatNumber(cafe, seatNumber);
        if (findSeat == null) {
            log.error("없는 좌석입니다.");
            return false;
        } else if (findSeat.isReserved()) {
            log.error("이미 예약 된 좌석입니다.");
            return false;
        }

        //사용가능한 Ticket 검색 (유효성 확인도 진행)
        Optional<Ticket> ticketOpt = ticketRepository.findAvailableTicketByIdAndCafe(user.getId(), cafe.getCafeName());
        if (ticketOpt.isEmpty()) {
            log.error("사용 가능한 티켓이 없습니다.");
            return false;
        }

        //기존에 차지하고 있던 자리가 있으면 exit
        exitSeat(userId);

        //seat 에 User, ticket 을 할당
        findSeat.reserveSeat(user, ticketOpt.get());

        //ReservationInfo 저장
        SeatReservationInfo reservationInfo = SeatReservationInfo.builder()
                .user(user)
                .cafe(cafe)
                .seat(findSeat)
                .ticket(ticketOpt.get())
                .startTime(startTime)
                .build();
        reservationInfoRepository.save(reservationInfo);

        log.info("좌석 예약 성공");
        return true;
    }

    @Override
    public int updateAllReservedSeatState() {
        int count = 0;
        count += seatRepository.updateAllReservedSeatStateWithFixedTermTicket();
        count += seatRepository.updateAllReservedSeatStateWithPartTimeTicket();
        count += seatRepository.updateAllReservedSeatStateWithStartTime();

        return count;
    }

    private void alertFcm(List<Seat> list) {
        // TODO
        /* List<Seat>에서 FCM을 보낼 유저들을 특정 후 FireBase서버를 경유해 10분 남았다고 알림을 요청 */
        List<String> userName = list.stream()
                .map(u -> u.getUser().getLoginId())
                .collect(Collectors.toList());

        if (!userName.isEmpty()) {
            for (String userNames : userName) {
                String token = redisRepository.getValue(userNames + "_" + "USER" + "_FCM_TOKEN");
                try {
                    firebaseCloudMessageService.sendMessageToSpecificUser(token,
                            "알라딘 스터디카페",
                            "좌석이 10분 남았습니다.");
                } catch (IOException e) {
                    log.info("FCM Message sending failed");
                    e.printStackTrace();
                }
            }
        } else {
            log.info("alertFcm user is not exist");
        }
    }

    private void checkAlmostFinishedSeatWithFixedTermTicket() {
        List<Seat> list = seatRepository.getAlmostFinishedSeatListWithFixedTermTicket(10L);
        log.debug("Alert list size with FixedTerm Ticket = {}", list.size());

        alertFcm(list);
    }

    private void checkAlmostFinishedSeatWithStartTime() {
        List<Seat> list = seatRepository.getAlmostFinishedSeatListWithStartTime(10L);
        log.debug("Alert list size with StartTime = {}", list.size());

        alertFcm(list);
    }

    //1분 초과로 스케쥴 잡아야 중복 alert 없음
    public void alertAlmostFinishedSeat() {
        this.checkAlmostFinishedSeatWithFixedTermTicket();
        this.checkAlmostFinishedSeatWithStartTime();
    }
}
