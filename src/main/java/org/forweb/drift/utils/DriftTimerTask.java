package org.forweb.drift.utils;


import org.forweb.drift.dto.drift.PlayersToUpdate;
import org.forweb.drift.dto.drift.RoomDto;
import org.forweb.drift.entity.drift.BaseObject;
import org.forweb.drift.entity.drift.Player;
import org.forweb.drift.entity.drift.Room;

import java.io.IOException;
import java.util.Iterator;
import java.util.Set;
import java.util.TimerTask;

public class DriftTimerTask extends TimerTask {

    private Room me;

    public DriftTimerTask(Room room) {
        this.me = room;
    }


    @Override
    public void run() {
        long start = System.currentTimeMillis();
        System.out.println("start " + start);
        try {
            Set<BaseObject> objects = me.getObjects();
            Iterator<BaseObject> iterator = objects.iterator();
            while (iterator.hasNext()) {
                BaseObject obj = iterator.next();
                if (obj.isAlive()) {
                    obj.update();
                    if (obj.getX() > me.getX()) {
                        obj.setX(0);
                    } else if (obj.getX() < 0) {
                        obj.setX(me.getX());
                    }
                    if (obj.getY() > me.getY()) {
                        obj.setY(0);
                    } else if (obj.getY() < 0) {
                        obj.setY(me.getY());
                    }
                    me.calculateImpacts(obj);
                } else {
                    objects.remove(obj);
                }
            }

            String allObjects = null;
            String message;
            try {
                PlayersToUpdate playersToUpdate = new PlayersToUpdate(me.getPlayers());
                String playersUpdate = playersToUpdate.hasPlayers() ?
                        me.getMapper().writeValueAsString(playersToUpdate.getShips())
                        : null;
                for (Player player : me.getPlayers()) {

                    if (player.isFullUpdate()) {
                        player.setFullUpdate(false);
                        if (allObjects == null) {
                            allObjects = me.getMapper().writeValueAsString(new RoomDto(me, player));
                        }
                        message = allObjects;
                    } else {
                        message = playersUpdate;
                    }
                    if(message != null) {
                        player.getSession().getBasicRemote().sendText(message);
                    }

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        long end = System.currentTimeMillis();
        System.out.println("end " + end + "; " + (end -start));
    }
}

