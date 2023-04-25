package asc.portfolio.ascSb.push.domain;

import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.MulticastMessage;

import java.util.List;

public interface PushMessageSender {

    //Message 추상화하고 싶은데 너무 커서 대체할 수가 없다...
    void sendMessage(Message message);

    void sendAll(List<Message> messages);

    void sendMulticast(MulticastMessage message);
}
