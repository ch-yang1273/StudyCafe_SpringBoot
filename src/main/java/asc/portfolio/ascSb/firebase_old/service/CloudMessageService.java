package asc.portfolio.ascSb.firebase_old.service;

import java.io.IOException;

public interface CloudMessageService {

    void sendMessageToSpecificUser(String targetToken, String title, String body) throws IOException;
}
