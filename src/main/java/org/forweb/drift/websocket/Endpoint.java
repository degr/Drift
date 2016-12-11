package org.forweb.drift.websocket;

import org.springframework.web.socket.server.standard.SpringConfigurator;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.EOFException;

@ServerEndpoint(value = "/commandos", configurator = SpringConfigurator.class)
public class Endpoint {


    public static final int PERSON_RADIUS = 20;
    public static final int ROCKET_RADIUS = 8;
    public static final int FIRE_RADIUS = 8;
    public static final Integer LIFE_AT_START = 100;

    public static final long TICK_DELAY = 10;
    public static final double MOVEMENT_SPEED = 2;
    public static final int SKIP_FRAMES = 1;

    public static final int CLUSTER_SIZE = 100;



    private int id;
    private int roomId;
    private Session session;

    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
    }

    @OnMessage
    public void onTextMessage(String message) {
    }



    @OnClose
    public void onClose() {
    }

    @OnError
    public void onError(Throwable t) throws Throwable {
        // Most likely cause is a user closing their browser. Check to see if
        // the root cause is EOF and if it is ignore it.
        // Protect against infinite loops.
        int count = 0;
        Throwable root = t;
        while (root.getCause() != null && count < 20) {
            root = root.getCause();
            count++;
        }
        if (!(root instanceof EOFException)) {
            throw t;
        }
    }
}