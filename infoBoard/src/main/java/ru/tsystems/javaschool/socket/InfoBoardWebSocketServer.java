package ru.tsystems.javaschool.socket;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.websocket.OnClose;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint("/actions")
@ApplicationScoped
public class InfoBoardWebSocketServer {

    @Inject
    private InfoBoardSessionHandler sessionHandler;

    @OnOpen
    public void open(Session session) {
        sessionHandler.addSession(session);
    }


    @OnClose
    public void close(Session session) {
        sessionHandler.removeSession(session);
    }
}
