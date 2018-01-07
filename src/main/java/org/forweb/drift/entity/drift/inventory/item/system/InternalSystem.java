package org.forweb.drift.entity.drift.inventory.item.system;

import org.forweb.drift.entity.drift.PolygonalObject;
import org.forweb.drift.entity.drift.PolygonalObjectEntity;
import org.forweb.drift.entity.drift.inventory.item.Inventory;
import org.forweb.drift.entity.drift.spaceships.PolygonalSpaceShip;
import org.forweb.geometry.shapes.Point;

public abstract class InternalSystem extends Inventory {
    public InternalSystem(PolygonalObjectEntity configuration, double bulk) {
        super(configuration, bulk);
    }

    public abstract void affect(PolygonalSpaceShip spaceShip);

    @Override
    public PolygonalObject[] generate() {
        return null;
    }

    @Override
    public PolygonalObject[] onImpact(PolygonalObject that, Point impact) {
        return null;
    }
}
