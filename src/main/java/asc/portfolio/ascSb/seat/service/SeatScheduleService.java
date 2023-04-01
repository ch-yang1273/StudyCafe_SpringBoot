package asc.portfolio.ascSb.seat.service;

import asc.portfolio.ascSb.common.domain.CommonEventsPublisher;
import asc.portfolio.ascSb.common.domain.CurrentTimeProvider;
import asc.portfolio.ascSb.seat.domain.Seat;
import asc.portfolio.ascSb.seat.domain.SeatRepository;
import asc.portfolio.ascSb.seat.domain.SeatUsageStatus;
import asc.portfolio.ascSb.seat.dto.SeatUsageEndingSoonEvent;
import asc.portfolio.ascSb.seat.dto.SeatUsageTerminatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SeatScheduleService {

    private final SeatRepository seatRepository;
    private final CommonEventsPublisher eventsPublisher;
    private final CurrentTimeProvider currentTimeProvider;

    @Transactional
    public void updateApproachingExpiredSeatsStatus() {
        LocalDateTime now = currentTimeProvider.localDateTimeNow();
        List<Seat> seats = seatRepository.findSeatsByStatusWithEndTimeAfter(
                SeatUsageStatus.IN_USE,
                now.minusMinutes(10));

        for (Seat seat : seats) {
            seat.changeUsageStatusEndingSoon();
            eventsPublisher.raise(new SeatUsageEndingSoonEvent(seat.getId(), now));
        }
    }

    @Transactional
    public void terminateExpiredSeatsStatus() {
        LocalDateTime now = currentTimeProvider.localDateTimeNow();
        List<Seat> seats = seatRepository.findSeatsByStatusWithEndTimeAfter(
                SeatUsageStatus.ENDING_SOON,
                now);

        for (Seat seat : seats) {
            seat.scheduleSeatUsageTermination();
            eventsPublisher.raise(new SeatUsageTerminatedEvent(seat.getId(), now));
        }
    }

//    private void alertFcm(List<Seat> list) {
//        // TODO
//        /* List<Seat>에서 FCM을 보낼 유저들을 특정 후 FireBase서버를 경유해 10분 남았다고 알림을 요청 */
//        List<Long> userIds = list.stream()
//                .map(Seat::getUserId)
//                .collect(Collectors.toList());
//
//        for (Long id : userIds) {
//            User user = userFinder.findById(id);
//            // todo 필수 값이 아닌 userName으로 검색을하고 있다. loginId로 변경
//            String token = redisRepository.getValue(user.getName() + "_" + "USER" + "_FCM_TOKEN");
//            try {
//                firebaseCloudMessageService.sendMessageToSpecificUser(token,
//                        "알라딘 스터디카페",
//                        "좌석이 10분 남았습니다.");
//            } catch (IOException e) {
//                log.info("FCM Message sending failed");
//                e.printStackTrace();
//            }
//        }
//    }
}
