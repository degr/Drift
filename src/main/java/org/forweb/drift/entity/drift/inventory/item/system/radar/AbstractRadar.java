package org.forweb.drift.entity.drift.inventory.item.system.radar;

import org.forweb.drift.entity.drift.inventory.item.system.InternalSystem;
import org.forweb.drift.entity.drift.spaceships.PolygonalSpaceShip;
import org.jbox2d.common.Vec2;

public abstract class AbstractRadar extends InternalSystem {
    public AbstractRadar(Vec2[] configuration, double bulk) {
        super(configuration, bulk);
    }

    public abstract double getRadius();

    public abstract boolean canScan();

    @Override
    public void affect(PolygonalSpaceShip spaceShip) {
    }


    @Override
    public void mount(PolygonalSpaceShip spaceShip) {

    }

    @Override
    public void unMount(PolygonalSpaceShip spaceShip) {

    }
}
