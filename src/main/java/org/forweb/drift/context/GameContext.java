package org.forweb.drift.context;

import org.forweb.drift.entity.drift.Asteroid;
import org.forweb.drift.entity.drift.BaseObject;
import org.forweb.drift.entity.drift.Room;
import org.forweb.drift.entity.drift.controlable.base.AbstractBase;
import org.forweb.drift.entity.drift.controlable.base.RefinaryBase;
import org.forweb.drift.services.AsteroidService;
import org.forweb.drift.utils.IncrementalId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Service
public class GameContext {


    @Autowired
    AsteroidService asteroidService;

    private Room room;

    @PostConstruct
    public void postConstruct() {
        IncrementalId ids = new IncrementalId();
        int x = 3000;
        int y = 3000;
        List<Asteroid> asteroidList = asteroidService.createMulty(1, x, y, ids);
        List<AbstractBase> basesList = new ArrayList<>();
        basesList.add(new RefinaryBase(500, 500, 0.754, ids.get()));
        List<BaseObject> baseObjects = new ArrayList<>(asteroidList);
        baseObjects.addAll(basesList);
        room = new Room(x, y, baseObjects, ids);
    }

    public Room getRoom() {
        return room;
    }
}
