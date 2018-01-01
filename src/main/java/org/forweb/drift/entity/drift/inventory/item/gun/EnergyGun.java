package org.forweb.drift.entity.drift.inventory.item.gun;

import org.forweb.drift.entity.drift.PolygonalObjectEntity;

public abstract class EnergyGun extends Gun {
    public EnergyGun(PolygonalObjectEntity configuration) {
        super(configuration);
    }

    @Override
    public boolean isEnergy() {
        return true;
    }
}
