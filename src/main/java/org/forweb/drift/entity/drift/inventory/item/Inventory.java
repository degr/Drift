package org.forweb.drift.entity.drift.inventory.item;

import org.forweb.drift.entity.drift.PolygonalObject;
import org.forweb.drift.entity.drift.PolygonalObjectEntity;
import org.forweb.drift.entity.drift.spaceships.PolygonalSpaceShip;

import java.awt.*;

public abstract class Inventory extends PolygonalObject{
    private double bulk;

    public Inventory(PolygonalObjectEntity configuration, double bulk) {
        super(configuration);
        this.bulk = bulk;
    }

    public abstract int getEnergyConsumption();

    public double getBulk() {
        return bulk;
    }
    public abstract void mount(PolygonalSpaceShip spaceShip);
    public abstract void unMount(PolygonalSpaceShip spaceShip);

    @Override
    public boolean isAlive() {
        return true;
    }

    @Override
    public boolean isInvincible() {
        return false;
    }
}
