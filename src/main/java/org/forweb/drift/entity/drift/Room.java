package org.forweb.drift.entity.drift;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.forweb.drift.utils.AngleSerializer;
import org.forweb.drift.utils.DriftTimerTask;
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
        return player;
    }

    private void startTimer() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new DriftTimerTask(this), TICK_DELAY, TICK_DELAY);
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

    public void calculateImpacts(BaseObject obj) {
        Iterator<BaseObject> iterator = this.objects.iterator();
        while(iterator.hasNext()) {
            BaseObject obj1 = iterator.next();
            if(obj1 != obj) {
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
                            Collections.addAll(this.objects, imp1);
                        }
                        if(imp2 != null && imp2.length > 0) {
                            Collections.addAll(this.objects, imp2);
                        }
                    }
                }
            }
        }
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
        objects.remove(player.getSpaceShip());
        if (players.isEmpty()) {
            timer.cancel();
        }
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
}
