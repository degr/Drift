package org.forweb.drift.context;

import org.forweb.drift.entity.drift.Room;
import org.springframework.stereotype.Service;

@Service
public class GameContext {
    private Room room;

    GameContext() {
        room = new Room(5000, 5000);
    }

    public Room getRoom() {
        return room;
    }
}
