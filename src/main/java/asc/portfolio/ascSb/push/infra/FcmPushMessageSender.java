package asc.portfolio.ascSb.push.infra;

import asc.portfolio.ascSb.push.domain.PushMessageSender;
import com.google.firebase.messaging.BatchResponse;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.MulticastMessage;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class FcmPushMessageSender implements PushMessageSender {

    @Override
    public void sendMessage(Message message) {
        try {
            String response = FirebaseMessaging.getInstance().send(message);
            log.info("Successfully sent firebase message: {}", response);
        } catch (FirebaseMessagingException e) {
            throw new RuntimeException("FirebaseMessagingException", e);
        }
    }

    @Override
    public void sendAll(List<Message> messages) {
        try {
            BatchResponse response = FirebaseMessaging.getInstance().sendAll(messages);
            log.info("{} messages were sent successfully", response.getSuccessCount());
        } catch (FirebaseMessagingException e) {
            throw new RuntimeException("FirebaseMessagingException", e);
        }
    }

    @Override
    public void sendMulticast(MulticastMessage message) {
        try {
            BatchResponse response = FirebaseMessaging.getInstance().sendMulticast(message);
            log.info("{} messages were sent successfully", response.getSuccessCount());
        } catch (FirebaseMessagingException e) {
            throw new RuntimeException("FirebaseMessagingException", e);
        }
    }
}
