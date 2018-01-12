package org.forweb.drift.entity.drift.inventory.item;

import org.forweb.drift.entity.drift.spaceships.PolygonalSpaceShip;
import org.jbox2d.common.Vec2;

import java.util.concurrent.atomic.AtomicInteger;

public abstract class Inventory {
    private final Vec2[] points;


    private final double angle;
    private double bulk;
    private static final AtomicInteger idGenerator = new AtomicInteger(0);
    private int id;

    public Inventory(Vec2[] points, double bulk) {
        this.points = points;
        this.bulk = bulk;
        angle = 0;
        id = idGenerator.incrementAndGet();
    }

    public abstract int getEnergyConsumption();

    public double getBulk() {
        return bulk;
    }

    public abstract void mount(PolygonalSpaceShip spaceShip);

    public abstract void unMount(PolygonalSpaceShip spaceShip);

    public Vec2[] getPoints() {
        return points;
    }

    public int getId() {
        return id;
    }
    public double getAngle() {
        return angle;
    }
}
