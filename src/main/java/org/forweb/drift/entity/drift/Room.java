package org.forweb.drift.entity.drift;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.forweb.drift.utils.AngleSerializer;
import org.forweb.drift.utils.ArrayUtils;
import org.forweb.drift.services.DriftTimerService;
import org.forweb.drift.utils.IncrementalId;
import org.forweb.geometry.misc.Angle;
import org.forweb.geometry.services.LineService;
import org.forweb.geometry.shapes.Point;

import javax.websocket.Session;
import java.util.*;

import static org.forweb.drift.websocket.Endpoint.CLUSTER_SIZE;
import static org.forweb.drift.websocket.Endpoint.TICK_DELAY;

public class Room {

    private Set<BaseObject> objects;
    private Set<Player> players;
    private double x;
    private double y;
    private Timer timer;
    private ObjectMapper mapper;
    private IncrementalId ids;

    private long lastFullUpdate = 0;

    public void setForceFullUpdate(boolean forceFullUpdate) {
        this.forceFullUpdate = forceFullUpdate;
    }

    private boolean forceFullUpdate = false;

    public Player newPlayer(Session session) {
        int id = ids.get();
        boolean nobody = players.isEmpty();
        Random random = new Random();
        Player player = new Player(
                id,
                session,
                new SpaceShip(random.nextInt((int)x), random.nextInt((int)y),id, this)
        );
        players.add(player);
        objects.add(player.getSpaceShip());
        if(nobody) {
            startTimer();
        }
        setForceFullUpdate(true);
        return player;
    }

    private void startTimer() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new DriftTimerService(this), TICK_DELAY, TICK_DELAY);
    }

    public Room(double x, double y, List<BaseObject> initialObjects, IncrementalId ids) {
        mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addSerializer(Angle.class, new AngleSerializer());
        mapper.registerModule(module);
        this.ids = ids;
        this.x = x;
        this.y = y;
        players = new HashSet<>();

        objects = new HashSet<>(initialObjects);
    }


    public BaseObject[] calculateImpacts(BaseObject obj) {
        Iterator<BaseObject> iterator = this.objects.iterator();
        BaseObject[] out = null;
        while(iterator.hasNext()) {
            BaseObject obj1 = iterator.next();
            if(obj1 != obj && obj1.isAlive()) {
                double distance = LineService.getDistance(
                        new Point(obj.getX(), obj.getY()),
                        new Point(obj1.getX(), obj1.getY())
                );
                if (distance < CLUSTER_SIZE) {
                    boolean impact = obj.hasImpact(obj1);
                    if (impact) {
                        BaseObject[] imp1 = obj.onImpact(obj1, ids);
                        BaseObject[] imp2 = obj1.onImpact(obj, ids);
                        if(imp1 != null && imp1.length > 0) {
                            if(out == null) {
                                out = imp1;
                            } else {
                                out = ArrayUtils.concat(out, imp2);
                            }
                        }
                        if(imp2 != null && imp2.length > 0) {
                            if(out == null) {
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


    public Set<BaseObject> getObjects() {
        return objects;
    }

    public void addObject(BaseObject object) {
        objects.add(object);
    }

    public void removePlayer(Player player) {
        if(player == null) {
            return;
        }
        players.remove(player);
        player.getSpaceShip().setAlive(false);
        if (players.isEmpty()) {
            timer.cancel();
        }
        setForceFullUpdate(true);
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public IncrementalId getIds() {
        return ids;
    }

    public Set<Player> getPlayers() {
        return players;
    }

    public ObjectMapper getMapper() {
        return mapper;
    }

    public void resetFullUpdate() {
        lastFullUpdate = System.currentTimeMillis();
        setForceFullUpdate(false);
    }

    public boolean isFullUpdate() {
        //force full update each 10 seconds
        return (System.currentTimeMillis() > lastFullUpdate + 10000) || forceFullUpdate;
    }
}
