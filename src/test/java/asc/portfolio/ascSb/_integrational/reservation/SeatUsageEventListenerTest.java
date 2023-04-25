package asc.portfolio.ascSb._integrational.reservation;

import asc.portfolio.ascSb.common.domain.CommonEventsPublisher;
import asc.portfolio.ascSb.reservation.service.SeatUsageEventListener;
import asc.portfolio.ascSb.seat.dto.SeatUsageEndingSoonEvent;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.event.ApplicationEvents;
import org.springframework.test.context.event.RecordApplicationEvents;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RecordApplicationEvents
@SpringBootTest
class SeatUsageEventListenerTest {

    @Autowired
    private CommonEventsPublisher eventsPublisher;

    @MockBean
    private SeatUsageEventListener listener;

    @Autowired
    private ApplicationEvents events;

    @Test
    void raise() {
        eventsPublisher.raise(new SeatUsageEndingSoonEvent(1L, 1L, LocalDateTime.now()));

        // 1. Mockito로 EventListener 가 호출되었는지 확인
        verify(listener, times(1)).handleEndingSoonEvent(any());

        // 2. @RecordApplicationEvents 발행된 SeatUsageEndingSoonEvent 횟수를 확인
        long count = events.stream(SeatUsageEndingSoonEvent.class).count();
        Assertions.assertThat(count).isEqualTo(1L);
    }
}