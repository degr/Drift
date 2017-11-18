package org.forweb.drift.context;

import org.forweb.drift.entity.drift.Asteroid;
import org.forweb.drift.entity.drift.BaseObject;
import org.forweb.drift.entity.drift.Room;
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
        List<Asteroid> asteroidList = asteroidService.createMulty(100, 5000, 5000, ids);
        List<BaseObject> baseObjects = new ArrayList<>(asteroidList);
        room = new Room(5000, 5000, baseObjects, ids);
    }

    public Room getRoom() {
        return room;
    }
}
