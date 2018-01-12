package org.forweb.drift.entity.drift.inventory.item.gun.energy;

import org.forweb.drift.entity.drift.inventory.item.gun.EnergyGun;
import org.jbox2d.common.Vec2;

public abstract class MinorEnergyGun extends EnergyGun {

    public MinorEnergyGun(Vec2[] configuration) {
        super(configuration);
    }

    @Override
    public boolean isTurret() {
        return false;
    }

}
