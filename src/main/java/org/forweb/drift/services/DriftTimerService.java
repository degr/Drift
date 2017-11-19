package org.forweb.drift.services;


import org.forweb.drift.dto.drift.InfoDto;
import org.forweb.drift.dto.PlayersDto;
import org.forweb.drift.dto.drift.PlayersToUpdate;
import org.forweb.drift.dto.drift.RoomDto;
import org.forweb.drift.entity.drift.*;
import org.forweb.drift.utils.ArrayUtils;

import javax.websocket.RemoteEndpoint;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class DriftTimerService extends TimerTask {

    private Room me;

    public DriftTimerService(Room room) {
        this.me = room;
    }


    @Override
    public void run() {
        try {
            Set<BaseObject> objects = me.getObjects();
            Iterator<BaseObject> iterator = objects.iterator();
            BaseObject[] newObjects = null;
            while (iterator.hasNext()) {
                BaseObject obj = iterator.next();

                if (obj.isAlive()) {
                    BaseObject[] generated = obj.update();
                    if(generated != null) {
                        if(newObjects == null) {
                            newObjects = generated;
                        } else {
                            newObjects = ArrayUtils.concat(newObjects, generated);
                        }
                    }
                    if (obj.getX() > me.getX()) {
                        obj.setX(obj.getX() - me.getX());
                    } else if (obj.getX() < 0) {
                        obj.setX(me.getX() + obj.getX());
                    }
                    if (obj.getY() > me.getY()) {
                        obj.setY(obj.getY() - me.getY());
                    } else if (obj.getY() < 0) {
                        obj.setY(me.getY() + obj.getY());
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
                    iterator.remove();
                }
            }


            if (newObjects != null) {
                int l = newObjects.length;
                while (l-- > 0){
                    objects.add(newObjects[l]);
                }
            }
           /* long aCount = objects.stream().filter(a -> a instanceof Asteroid && a.isAlive()).count();*/
            try {
                /*List<SpaceShip> ghosts = me.getPlayers().stream().map(v -> v.getSpaceShip()).collect(Collectors.toList());*/
                String feed;
                boolean hasAsteroids = false;
                if(me.isFullUpdate()) {
                    feed = me.getMapper().writeValueAsString(new RoomDto(me));
                } else {
                    PlayersToUpdate playersToUpdate = new PlayersToUpdate(me.getPlayers());
                    feed = newObjects != null || playersToUpdate.hasPlayers()/* || ghosts.size() > 0*/ ?
                            me.getMapper().writeValueAsString(new PlayersDto(playersToUpdate.getShips(), newObjects/*, ghosts, aCount*/))
                            : null;

                }
                for (Player player : me.getPlayers()) {
                    RemoteEndpoint.Basic remote = player.getSession().getBasicRemote();
                    if(player.isNeedInfo()) {
                        remote.sendText(me.getMapper().writeValueAsString(new InfoDto(player)));
                        player.setNeedInfo(false);
                    }

                    if (me.isFullUpdate()) {
                        remote.sendText(feed);
                    } else {
                        if (feed != null) {
                            remote.sendText(feed);
                        } else {
                            remote.sendText("1");
                        }
                    }
                }

                if(me.isFullUpdate()) {
                    me.resetFullUpdate();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

