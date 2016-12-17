package org.forweb.drift.entity.drift;

import javax.websocket.Session;
import java.io.IOException;
import java.util.*;

public class Room {
    private List<BaseObject> objects;
    private Set<Player> players;
    private double x;
    private double y;
    private Timer timer;
    private int idsCounter;

    public Player newPlayer(Session session) {
        idsCounter++;
        Random random = new Random();
        Player player = new Player(
                idsCounter,
                session,
                new SpaceShip(random.nextInt((int)x), random.nextInt((int)y), this)
        );
        players.add(player);
        return player;
    }


    public Room(double x, double y) {
        this.x = x;
        this.y = y;
        idsCounter = 0;
        players = new HashSet<>();
        objects = new ArrayList<>();
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                String message = "message";
                for(Player player : players) {
                    try {
                        player.getSession().getBasicRemote().sendText(message);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, 20, 20);
    }

    public void addObject(BaseObject object) {
        objects.add(object);
    }

    public void removePlayer(Player player) {
        players.remove(player);
    }
}
