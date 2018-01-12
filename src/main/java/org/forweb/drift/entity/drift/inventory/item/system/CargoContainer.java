package org.forweb.drift.entity.drift.inventory.item.system;

import org.forweb.drift.entity.drift.inventory.Cargo;
import org.forweb.drift.entity.drift.spaceships.PolygonalSpaceShip;
import org.jbox2d.common.Vec2;

public class CargoContainer extends InternalSystem {

    public CargoContainer(Vec2[] configuration) {
        super(configuration, 50);
    }

    @Override
    public int getEnergyConsumption() {
        return 0;
    }

    @Override
    public void affect(PolygonalSpaceShip spaceShip) {

    }

    @Override
    public void mount(PolygonalSpaceShip spaceShip) {
        Cargo cargoContainer = spaceShip.getCargo();
        cargoContainer.setAllowedBulk(cargoContainer.getAllowedBulk() + getBulk() * 0.8);
    }

    @Override
    public void unMount(PolygonalSpaceShip spaceShip) {
        Cargo cargoContainer = spaceShip.getCargo();
        cargoContainer.setAllowedBulk(cargoContainer.getAllowedBulk() - getBulk() * 0.8);
    }
}
