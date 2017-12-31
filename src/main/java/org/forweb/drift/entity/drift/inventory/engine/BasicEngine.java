package org.forweb.drift.entity.drift.inventory.engine;

import org.forweb.drift.entity.drift.PolygonalObjectEntity;
import org.forweb.geometry.misc.Vector;
import org.forweb.geometry.shapes.Point;

public class BasicEngine extends Engine{

    public BasicEngine() {
        super(new PolygonalObjectEntity(new Point[]{
                new Point(-2, 2), new Point(1, 1),
                new Point(1, -1), new Point(-2, -2)
        }, 0, 0, 0), 5);
    }

    @Override
    public double getPower() {
        return 10;
    }

    @Override
    public int getEnergyConsumption() {
        return 5;
    }
}
