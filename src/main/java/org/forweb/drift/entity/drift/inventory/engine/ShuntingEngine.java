package org.forweb.drift.entity.drift.inventory.engine;

import org.forweb.drift.entity.drift.PolygonalObjectEntity;
import org.forweb.geometry.shapes.Point;

public class ShuntingEngine extends Engine{

    public ShuntingEngine() {
        super(new PolygonalObjectEntity(new Point[]{
                new Point(-1, 1), new Point(1, 1),
                new Point(1, -1), new Point(-1, -1)
        }, 0, 0, 0), 2);
    }

    @Override
    public double getPower() {
        return 10;
    }
}
