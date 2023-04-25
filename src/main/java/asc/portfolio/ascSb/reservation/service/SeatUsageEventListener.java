package asc.portfolio.ascSb.reservation.service;

import asc.portfolio.ascSb.common.domain.CurrentTimeProvider;
import asc.portfolio.ascSb.push.domain.DeviceToken;
import asc.portfolio.ascSb.push.domain.DeviceTokenFinder;
import asc.portfolio.ascSb.push.domain.PushMessageSender;
import asc.portfolio.ascSb.reservation.domain.Reservation;
import asc.portfolio.ascSb.reservation.domain.ReservationFinder;
import asc.portfolio.ascSb.reservation.domain.ReservationLifecycleManager;
import asc.portfolio.ascSb.seat.domain.Seat;
import asc.portfolio.ascSb.seat.domain.SeatFinder;
import asc.portfolio.ascSb.seat.dto.SeatUsageEndingSoonEvent;
import asc.portfolio.ascSb.seat.dto.SeatUsageTerminatedEvent;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@RequiredArgsConstructor
@Service
public class SeatUsageEventListener {

    private final SeatFinder seatFinder;
    private final ReservationFinder reservationFinder;
    private final ReservationLifecycleManager reservationLifecycleManager;
    private final CurrentTimeProvider currentTimeProvider;

    private final PushMessageSender pushMessageSender;
    private final DeviceTokenFinder deviceTokenFinder;

    // todo: 두 Listener 다 userId 묶어서 전송하도록 수정 필요
    // Message 생성도 클래스 만들어야 함. 어디 둬야 할지 모르겠네...

    @EventListener
    public void handleEndingSoonEvent(SeatUsageEndingSoonEvent event) {
        log.info("Handling SeatUsageEndingSoonEvent: SeatId={}, TimeStamp={}", event.getSeatId(), event.getTimestamp());

        DeviceToken token = deviceTokenFinder.findById(event.getUserId());
        Message message = Message.builder()
                .setNotification(Notification.builder()
                        .setTitle("좌석 종료 예정 알림")
                        .setBody("좌석 종료가 10분 남았습니다.")
                        .build())
                .setToken(token.getToken())
                .build();

        pushMessageSender.sendMessage(message);
    }

    @TransactionalEventListener(SeatUsageTerminatedEvent.class)
    public void handlerTerminatedEvent(SeatUsageTerminatedEvent event) {
        Long seatId = event.getSeatId();

        Seat seat = seatFinder.findById(seatId);
        Reservation reservation = reservationFinder.findBySeatIdAndInUseStatus(seatId);

        reservationLifecycleManager.finish(seat.getUserId(), reservation, currentTimeProvider.localDateTimeNow());

        DeviceToken token = deviceTokenFinder.findById(event.getUserId());
        Message message = Message.builder()
                .setNotification(Notification.builder()
                        .setTitle("좌석 사용 종료 알림")
                        .setBody("좌석 사용이 종료되었습니다.")
                        .build())
                .setToken(token.getToken())
                .build();

        pushMessageSender.sendMessage(message);
    }
}
