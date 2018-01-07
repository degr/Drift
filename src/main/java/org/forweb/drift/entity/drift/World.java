package org.forweb.drift.entity.drift;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.forweb.drift.utils.ArrayUtils;
import org.forweb.drift.utils.PolygonalUtils;
import org.forweb.geometry.services.LineService;
import org.forweb.geometry.shapes.Point;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class World {
    private final double y;
    private final double x;


    private final ObjectMapper mapper;

    public List<PolygonalObject> getObjects() {
        return objects;
    }

    List<PolygonalObject> objects;
    PolygonalObject[][] impacts = null;

    public World(double width, double height) {
        objects = new ArrayList<>();
        x = width;
        y = height;
        mapper = new ObjectMapper();
    }

    void addObject(PolygonalObject object) {
        objects.add(object);
    }

    public void update() {
        List<PolygonalObject> objects = getObjects();
        Iterator<PolygonalObject> iterator = objects.iterator();
        PolygonalObject[] newObjects = null;
        while (iterator.hasNext()) {
            PolygonalObject obj = iterator.next();

            if (obj.isAlive()) {
                obj.update();
                PolygonalObject[] generated = obj.generate();
                if (generated != null) {
                    if (newObjects == null) {
                        newObjects = generated;
                    } else {
                        newObjects = ArrayUtils.concat(newObjects, generated);
                    }
                }
                if (obj.getX() > x) {
                    obj.setX(obj.getX() - x);
                } else if (obj.getX() < 0) {
                    obj.setX(x + obj.getX());
                }
                if (obj.getY() > y) {
                    obj.setY(obj.getY() - y);
                } else if (obj.getY() < 0) {
                    obj.setY(y + obj.getY());
                }
                PolygonalObject[] nObjects = calculateImpacts(obj);
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
        impacts = null;

        if (newObjects != null) {
            int l = newObjects.length;
            while (l-- > 0) {
                objects.add(newObjects[l]);
            }
        }
        /*try {
            String feed;
            if(isFullUpdate()) {
                feed = getMapper().writeValueAsString(new RoomDto(this));
            } else {
                PlayersToUpdate playersToUpdate = new PlayersToUpdate(getPlayers());
                feed = newObjects != null || playersToUpdate.hasPlayers() ?
                        getMapper().writeValueAsString(new PlayersDto(playersToUpdate.getShips(), newObjects))
                        : null;

            }
            for (Player player : getPlayers()) {
                RemoteEndpoint.Basic remote = player.getSession().getBasicRemote();
                if(player.isNeedInfo()) {
                    remote.sendText(getMapper().writeValueAsString(new InfoDto(player)));
                    player.setNeedInfo(false);
                }

                if (isFullUpdate()) {
                    remote.sendText(feed);
                } else {
                    if (feed != null) {
                        remote.sendText(feed);
                    } else {
                        remote.sendText("1");
                    }
                }
            }

            if(isFullUpdate()) {
                resetFullUpdate();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }

    public PolygonalObject[] calculateImpacts(PolygonalObject obj) {
        Iterator<PolygonalObject> iterator = this.objects.iterator();
        PolygonalObject[] out = null;
        while (iterator.hasNext()) {
            PolygonalObject obj1 = iterator.next();
            if (obj1 != obj && obj1.isAlive()) {
                double distance = LineService.getDistance(
                        obj.getX(), obj.getY(),
                        obj1.getX(), obj1.getY()
                );
                if (distance < obj.getRadius() + obj1.getRadius()) {
                    Point impact = obj.hasImpact(obj1);
                    if (impact != null && !alreadyImpacted(obj, obj1)) {
                        if(impacts == null) {
                            impacts = new PolygonalObject[8][2];
                        }
                        impacts = putImpacts(obj, obj1);

                        PolygonalUtils.resolveCollision(obj, obj1, impact);


                        PolygonalObject[] imp1 = obj.onImpact(obj1, impact);
                        PolygonalObject[] imp2 = obj1.onImpact(obj, impact);
                        if (imp1 != null && imp1.length > 0) {
                            if (out == null) {
                                out = imp1;
                            } else {
                                out = ArrayUtils.concat(out, imp2);
                            }
                        }
                        if (imp2 != null && imp2.length > 0) {
                            if (out == null) {
                                out = imp2;
                            } else {
                                out = ArrayUtils.concat(out, imp2);
                            }
                        }
                    }
                }
            }
        }
        return out;
    }

    private boolean alreadyImpacted(PolygonalObject obj, PolygonalObject obj1) {
        if(impacts == null) {
            return false;
        }
        for(int i = 0; i < impacts.length; i++) {
            if(impacts[i] == null) {
                return false;
            } else {
                if((impacts[i][0] == obj && impacts[i][1] == obj1) || (impacts[i][0] == obj1 && impacts[i][1] == obj)) {
                    return true;
                }
            }
        }
        return false;
    }

    private PolygonalObject[][] putImpacts(PolygonalObject obj, PolygonalObject obj1) {
        PolygonalObject[] impact = new PolygonalObject[2];
        impact[0] = obj;
        impact[1] = obj1;
        for(int i = 0; i < impacts.length; i++) {
            if(impacts[i][0] == null) {
                impacts[i] = impact;
                return impacts;
            }
        }
        PolygonalObject[][] out = ArrayUtils.concat(impacts, new PolygonalObject[8][2]);
        out[impacts.length] = impact;
        return out;
    }

    public ObjectMapper getMapper() {
        return mapper;
    }

    public void add(PolygonalObject object) {
        getObjects().add(object);
    }
}
