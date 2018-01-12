package org.forweb.drift.entity.drift.inventory.item.generator;

import org.forweb.drift.entity.drift.inventory.item.system.InternalSystem;
import org.forweb.drift.entity.drift.spaceships.PolygonalSpaceShip;
import org.jbox2d.common.Vec2;

public abstract class AbstractGenerator extends InternalSystem {
    public AbstractGenerator(Vec2[] configuration, double bulk) {
        super(configuration, bulk);
    }

    public abstract int getEnergyProduction();

    @Override
    public int getEnergyConsumption() {
        return 0;
    }

    @Override
    public void affect(PolygonalSpaceShip spaceShip) {
    }

    @Override
    public void mount(PolygonalSpaceShip spaceShip) {
        spaceShip.setEnergyRegeneration(spaceShip.getEnergyRegeneration() + getEnergyProduction());
    }

    @Override
    public void unMount(PolygonalSpaceShip spaceShip) {
        spaceShip.setEnergyRegeneration(spaceShip.getEnergyRegeneration() - getEnergyProduction());
    }
}
