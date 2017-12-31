package org.forweb.drift.entity.drift.inventory.generator;

import org.forweb.drift.entity.drift.PolygonalObjectEntity;
import org.forweb.drift.entity.drift.spaceships.PolygonalSpaceShip;
import org.forweb.geometry.shapes.Point;

public class BasicGenerator extends AbstractGenerator {
    public BasicGenerator() {
        super(new PolygonalObjectEntity(new Point[]{
                new Point(-1, 1),new Point(1, 1),new Point(1, -1),new Point(-1, -1),
        }, 0, 0, 0), 10);
    }

    @Override
    public void affect(PolygonalSpaceShip spaceShip) {
        spaceShip.addEnergy(4);
    }

}
