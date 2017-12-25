package org.forweb.drift.entity.drift.inventory.gun;

import org.forweb.drift.entity.drift.PolygonalObjectEntity;
import org.forweb.drift.entity.drift.inventory.Inventory;
import org.forweb.drift.entity.drift.inventory.ammo.Ammo;

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
}
