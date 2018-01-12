package org.forweb.drift.entity.drift.inventory.item.system;

import org.forweb.drift.entity.drift.inventory.item.Inventory;
import org.forweb.drift.entity.drift.spaceships.PolygonalSpaceShip;
import org.jbox2d.common.Vec2;

public abstract class InternalSystem extends Inventory {
    public InternalSystem(Vec2[] points, double bulk) {
        super(points, bulk);
    }

    public abstract void affect(PolygonalSpaceShip spaceShip);

}
