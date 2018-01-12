package org.forweb.drift.entity.drift.inventory.item.gun;

import org.jbox2d.common.Vec2;

public abstract class EnergyGun extends Gun {
    public EnergyGun(Vec2[] configuration) {
        super(configuration, 0);
    }

    @Override
    public boolean isEnergy() {
        return true;
    }
}
