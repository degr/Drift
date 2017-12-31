package org.forweb.drift.entity.drift.inventory.system;

import org.forweb.drift.entity.drift.PolygonalObjectEntity;
import org.forweb.drift.entity.drift.inventory.Inventory;
import org.forweb.drift.entity.drift.spaceships.PolygonalSpaceShip;

public class CargoContainer extends InternalSystem {

    public CargoContainer(PolygonalObjectEntity configuration) {
        super(configuration, 50);
    }

    @Override
    public int getEnergyConsumption() {
        return 0;
    }

    @Override
    public void affect(PolygonalSpaceShip spaceShip) {

    }
}
