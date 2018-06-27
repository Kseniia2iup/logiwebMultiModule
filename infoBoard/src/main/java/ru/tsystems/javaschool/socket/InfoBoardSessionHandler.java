package ru.tsystems.javaschool.socket;

import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.tsystems.javaschool.bean.JsonReceiverBean;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.websocket.Session;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@ApplicationScoped
public class InfoBoardSessionHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(InfoBoardSessionHandler.class);

    @Inject
    private JsonReceiverBean receiverBean;

    private final Set<Session> sessions = new HashSet<>();

    public void addSession(Session session) {
        sessions.add(session);
    }

    public void removeSession(Session session) {
        sessions.remove(session);
    }

    public void notifySessions() {
        JSONObject object = receiverBean.getInfoForUpdate();
        LOGGER.info("From InfoBoardSessionHandler receive JSONObject: \n {}", object.toJSONString());
        for (Session session : sessions) {
            sendMessage(session, object.toJSONString());
        }
    }

    private void sendMessage(Session session, String message) {
        try {
            session.getBasicRemote().sendText(message);
        } catch (IOException e) {
            sessions.remove(session);
        }
    }
}
