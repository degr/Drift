package org.forweb.drift.entity.drift;

import javax.websocket.Session;

public class Player {
    private int id;
    private SpaceShip spaceShip;


    private boolean fullUpdate;
    private Session session;
    private boolean needInfo;

    public Player(int id, Session session, SpaceShip spaceShip) {
        this.spaceShip = spaceShip;
        this.id = id;
        this.session = session;
        fullUpdate = true;


        /*new Thread(() -> {
            while(spaceShip.isAlive()) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                fullUpdate = true;
            }
        }).start();*/
    }

    public Session getSession() {
        return session;
    }

    public boolean isFullUpdate() {
        return fullUpdate;
    }

    @Override
    public int hashCode() {
        return id;
    }

    public String toString() {
        return "Player " + id;
    }

    public SpaceShip getSpaceShip() {
        return spaceShip;
    }

    public void setFullUpdate(boolean fullUpdate) {
        this.fullUpdate = fullUpdate;
    }

    public int getId() {
        return id;
    }

    public void setNeedInfo(boolean needInfo) {
        this.needInfo = needInfo;
    }

    public boolean isNeedInfo() {
        return needInfo;
    }
}
