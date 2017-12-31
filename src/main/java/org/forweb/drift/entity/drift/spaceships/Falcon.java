package org.forweb.drift.entity.drift.spaceships;

import org.forweb.drift.entity.drift.PolygonalObjectEntity;
import org.forweb.drift.entity.drift.inventory.Inventory;
import org.forweb.drift.entity.drift.inventory.engine.Engine;
import org.forweb.drift.entity.drift.inventory.gun.Gun;
import org.forweb.drift.entity.drift.inventory.slot.engine.MainEngineSlot;
import org.forweb.drift.entity.drift.inventory.slot.engine.ShuntingEngineSlot;
import org.forweb.drift.entity.drift.inventory.slot.gun.MinorEnergySlot;
import org.forweb.drift.entity.drift.inventory.slot.system.GeneratorSlot;
import org.forweb.drift.entity.drift.inventory.slot.system.SystemSlot;
import org.forweb.drift.entity.drift.spaceships.constants.CARGO;
import org.forweb.drift.entity.drift.spaceships.constants.ENERGY;
import org.forweb.drift.entity.drift.spaceships.constants.POINTS;
import org.forweb.geometry.shapes.Point;

public class Falcon extends PolygonalSpaceShip {

    public Falcon(double x, double y) {
        super(new PolygonalObjectEntity(
                        POINTS.FALCON.points,
                        x,
                        y,
                        0d,
                        CARGO.FALCON.amount
                ),
                ENERGY.FALCON.amount
        );
        Point[] points = this.getRelativePoints();
        addInventorySlot(new MainEngineSlot(
                new PolygonalObjectEntity(points[0].getX(), 0, 0),
                this
        ));
        addInventorySlot(new ShuntingEngineSlot(
                new PolygonalObjectEntity(points[2].getX() - 5, -3.5, Math.PI * 2.5 / 4),
                this
        ));
        addInventorySlot(new ShuntingEngineSlot(
                new PolygonalObjectEntity(points[2].getX() - 5, 3.5, -Math.PI * 2.5 / 4),
                this
        ));
        addInventorySlot(new GeneratorSlot(
                new PolygonalObjectEntity(points[2].getX() - 24, points[2].getY(), 0),
                this
        ));
        addInventorySlot(new SystemSlot(
                new PolygonalObjectEntity(points[0].getX() + 7, points[0].getY() + 5, 0),
                this
        ));
        addInventorySlot(new SystemSlot(
                new PolygonalObjectEntity(points[1].getX() + 7, points[1].getY() - 5, 0),
                this
        ));
        addInventorySlot(new MinorEnergySlot(
                new PolygonalObjectEntity(points[0].getX() + 5, points[0].getY() + 3, 0),
                this
        ));
        addInventorySlot(new MinorEnergySlot(
                new PolygonalObjectEntity(points[1].getX() + 5, points[1].getY() - 3, 0),
                this
        ));
    }

}
