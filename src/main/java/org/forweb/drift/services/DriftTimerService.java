package org.forweb.drift.services;


import org.forweb.drift.dto.PlayersDto;
import org.forweb.drift.dto.drift.PlayersToUpdate;
import org.forweb.drift.dto.drift.RoomDto;
import org.forweb.drift.entity.drift.*;
import org.forweb.drift.utils.ArrayUtils;

import java.io.IOException;
import java.util.Iterator;
import java.util.Set;
import java.util.TimerTask;

public class DriftTimerService extends TimerTask {

    private Room me;

    public DriftTimerService(Room room) {
        this.me = room;
    }

    //public static boolean GAME_IN_PLAY = true;

    @Override
    public void run() {
        try {
            Set<BaseObject> objects = me.getObjects();
            Iterator<BaseObject> iterator = objects.iterator();
            BaseObject[] newObjects = null;
            while (iterator.hasNext()) {
                BaseObject obj = iterator.next();
                /*if(!GAME_IN_PLAY && obj instanceof Bullet) {
                    System.out.println("bullet");
                }*/
                if (obj.isAlive()) {
                    //if(GAME_IN_PLAY) {
                        obj.update();
                    //}
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
                    BaseObject[] nObjects = me.calculateImpacts(obj);
                    if (nObjects != null) {
                        if (newObjects == null) {
                            newObjects = nObjects;
                        } else {
                            newObjects = ArrayUtils.concat(newObjects, nObjects);
                        }
                    }
                } else {
                    if(obj instanceof SpaceShip) {
                        System.out.println("lol");
                    }
                    iterator.remove();
                }
            }


            if (newObjects != null) {
                for (BaseObject object : newObjects) {
                    objects.add(object);
                }
            }

            String allObjects = null;
            String message;
            try {
                PlayersToUpdate playersToUpdate = new PlayersToUpdate(me.getPlayers());
                String playersUpdate = newObjects != null || playersToUpdate.hasPlayers() ?
                        me.getMapper().writeValueAsString(new PlayersDto(playersToUpdate.getShips(), newObjects))
                        : null;

                for (Player player : me.getPlayers()) {

                    if (player.isFullUpdate()) {
                        player.setFullUpdate(false);
                        if (allObjects == null) {
                            allObjects = me.getMapper().writeValueAsString(new RoomDto(me, player));
                        }
                        message = allObjects;

                        if (message != null) {
                            player.getSession().getBasicRemote().sendText(message);
                        }
                    } else {
                        message = playersUpdate;
                        if (message != null) {
                            player.getSession().getBasicRemote().sendText(message);
                        }
                    }
                }


            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

