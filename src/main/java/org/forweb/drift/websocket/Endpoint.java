package org.forweb.drift.websocket;

import org.forweb.drift.context.GameContext;
import org.forweb.drift.entity.drift.Player;
import org.forweb.drift.entity.drift.Room;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.server.standard.SpringConfigurator;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.EOFException;

@ServerEndpoint(value = "/drift", configurator = SpringConfigurator.class)
public class Endpoint {


    public static final long TICK_DELAY = 20;

    public static final int CLUSTER_SIZE = 300;

    private Player player;
    private Room room;
    @Autowired
    GameContext gameContext;

    @OnOpen
    public void onOpen(Session session) {
        this.room = gameContext.getRoom();
        this.player = this.room.newPlayer(session);
    }

    @OnMessage
    public void onTextMessage(String message) {
        String[] parts = message.split(":");
        switch (parts[0]) {
            case "turn":
                player.getSpaceShip().setTurn("1".equals(parts[1]) ? 1 : ("0".equals(parts[1]) ? 0 : -1));
                break;
            case "accelerate":
                player.getSpaceShip().setHasAcceleration("1".equals(parts[1]));
                break;
            case "fire":
                player.getSpaceShip().setFireStarted("1".equals(parts[1]));
                break;
        }
    }


    @OnClose
    public void onClose() {
        room.removePlayer(this.player);
    }

    @OnError
    public void onError(Throwable t) throws Throwable {
        room.removePlayer(this.player);
        // Most likely cause is a user closing their browser. Check to see i
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