package org.forweb.drift.entity.drift.inventory.system;

import org.forweb.drift.entity.drift.PolygonalObjectEntity;
import org.forweb.drift.entity.drift.inventory.Inventory;
import org.forweb.drift.entity.drift.spaceships.PolygonalSpaceShip;

public abstract class AbstractRadar extends InternalSystem {
    public AbstractRadar(PolygonalObjectEntity configuration, double bulk) {
        super(configuration, bulk);
    }

    public abstract double getRadius();
    public abstract boolean canScan();

    @Override
    public void affect(PolygonalSpaceShip spaceShip) {
    }
}
