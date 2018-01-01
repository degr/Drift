package org.forweb.drift.entity.drift.inventory.item.gun;

import org.forweb.drift.entity.drift.PolygonalObjectEntity;
import org.forweb.drift.entity.drift.inventory.item.Inventory;
import org.forweb.drift.entity.drift.inventory.item.ammo.Ammo;
import org.forweb.drift.entity.drift.spaceships.PolygonalSpaceShip;

public abstract class Gun extends Inventory{

    private int ammoLimit;
    private int ammoCount;


    public Gun(PolygonalObjectEntity configuration) {
        super(configuration, 1);
        ammoLimit = (int)configuration.getCapacity();
        ammoCount = 0;
    }

    public abstract boolean isTurret();
    public abstract Ammo fire();
    public abstract boolean isEnergy();
    public int ammoCount() {
        return ammoCount;
    }

    public int charge(int ammoCount) {
        this.ammoCount += ammoCount;
        if(this.ammoCount > ammoLimit) {
            int delta = this.ammoCount - ammoLimit;
            this.ammoCount = ammoLimit;
            return delta;
        } else {
            return 0;
        }
    }

    @Override
    public void mount(PolygonalSpaceShip spaceShip) {

    }

    @Override
    public void unMount(PolygonalSpaceShip spaceShip) {

    }
}
