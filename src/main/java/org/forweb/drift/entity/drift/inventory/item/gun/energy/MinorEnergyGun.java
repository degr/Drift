package org.forweb.drift.entity.drift.inventory.item.gun.energy;

import org.forweb.drift.entity.drift.PolygonalObjectEntity;
import org.forweb.drift.entity.drift.inventory.item.gun.EnergyGun;

public abstract class MinorEnergyGun extends EnergyGun {

    public MinorEnergyGun(PolygonalObjectEntity configuration) {
        super(configuration);
    }

    @Override
    public boolean isTurret() {
        return false;
    }

}
