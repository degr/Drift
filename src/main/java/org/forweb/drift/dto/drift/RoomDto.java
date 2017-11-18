package org.forweb.drift.dto.drift;

import org.forweb.drift.entity.drift.BaseObject;
import org.forweb.drift.entity.drift.Player;
import org.forweb.drift.entity.drift.Room;

import java.util.Set;

public class RoomDto implements UpdateDto {

    private Set<BaseObject> objects;
    private double x;
    private double y;

    public RoomDto(Room room) {
        objects = room.getObjects();
        x = room.getX();
        y = room.getY();
    }

    public Set<BaseObject> getObjects() {
        return objects;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    @Override
    public String getType() {
        return "fullUpdate";
    }
}
