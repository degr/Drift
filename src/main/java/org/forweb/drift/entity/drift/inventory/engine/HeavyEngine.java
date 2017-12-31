package org.forweb.drift.entity.drift.inventory.engine;

import org.forweb.drift.entity.drift.PolygonalObjectEntity;
import org.forweb.geometry.shapes.Point;

public class HeavyEngine extends Engine{

    public HeavyEngine() {
        super(new PolygonalObjectEntity(new Point[]{
                new Point(-2, 3), new Point(1, 2),
                new Point(1, -2), new Point(-2, -3)
        }, 0, 0, 0), 10);
    }

    @Override
    public double getPower() {
        return 25;
    }

    @Override
    public int getEnergyConsumption() {
        return 10;
    }
}
