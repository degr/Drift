package org.forweb.drift.entity.drift.inventory.gun;

import org.forweb.drift.entity.drift.PolygonalObjectEntity;
import org.forweb.drift.entity.drift.inventory.ammo.Ammo;

public abstract class EnergyGun extends Gun {
    public EnergyGun(PolygonalObjectEntity configuration) {
        super(configuration);
    }

    @Override
    public boolean isEnergy() {
        return true;
    }
}
