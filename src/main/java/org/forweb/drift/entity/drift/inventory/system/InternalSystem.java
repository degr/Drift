package org.forweb.drift.entity.drift.inventory.system;

import org.forweb.drift.entity.drift.PolygonalObjectEntity;
import org.forweb.drift.entity.drift.inventory.Inventory;
import org.forweb.drift.entity.drift.spaceships.PolygonalSpaceShip;

public abstract class InternalSystem extends Inventory {
    public InternalSystem(PolygonalObjectEntity configuration, double bulk) {
        super(configuration, bulk);
    }

    public abstract void affect(PolygonalSpaceShip spaceShip);
}
