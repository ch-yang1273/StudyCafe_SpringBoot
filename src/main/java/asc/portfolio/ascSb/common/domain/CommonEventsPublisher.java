package asc.portfolio.ascSb.common.domain;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
public class CommonEventsPublisher {

    private final ApplicationEventPublisher publisher;

    public CommonEventsPublisher(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    public void raise(Object event) {
        publisher.publishEvent(event);
    }
}
