package org.forweb.drift.entity.drift;

import javax.websocket.Session;

public class Player {
    private int id;
    private SpaceShip spaceShip;
    private boolean fullUpdate;
    private Session session;

    public Player(int id, Session session, SpaceShip spaceShip) {
        this.spaceShip = spaceShip;
        this.id = id;
        this.session = session;
        fullUpdate = true;
    }

    public Session getSession() {
        return session;
    }

    @Override
    public int hashCode() {
        return id;
    }
}
