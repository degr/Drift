package org.forweb.drift.websocket;

import org.forweb.drift.context.GameContext;
import org.forweb.drift.entity.drift.Player;
import org.forweb.drift.entity.drift.Room;
import org.forweb.drift.entity.drift.SpaceShip;
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
        SpaceShip spaceShip = player.getSpaceShip();
        switch (parts[0]) {
            case "turn":
                if(spaceShip.isInvincible()) {
                    spaceShip.setInvincible(false);
                }
                int turn = "1".equals(parts[1]) ? 1 : ("0".equals(parts[1]) ? 0 : -1);
                if(spaceShip.getTurn() != turn) {
                    spaceShip.setTurn(turn);
                    spaceShip.setUpdateTurn(true);
                    spaceShip.setUpdateRequire(true);
                }
                break;
            case "accelerate":
                if(spaceShip.isInvincible()) {
                    spaceShip.setInvincible(false);
                }
                boolean hasAcceleration = "1".equals(parts[1]);
                if(spaceShip.isHasAcceleration() != hasAcceleration) {
                    spaceShip.setHasAcceleration(hasAcceleration);
                    spaceShip.setUpdateRequire(true);
                }
                break;
            case "fire":
                if(spaceShip.isInvincible()) {
                    spaceShip.setInvincible(false);
                }
                boolean fireStarted = "1".equals(parts[1]);
                if(spaceShip.isFireStarted() != fireStarted) {
                    spaceShip.setFireStarted(fireStarted);
                    spaceShip.setUpdateRequire(true);
                }
                break;
        }
    }

    private void onInvincible(SpaceShip spaceShip) {
        if(spaceShip.isInvincible()) {
            spaceShip.setInvincible(false);
            spaceShip.setUpdateInvincible(true);
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