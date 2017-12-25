package org.forweb.drift.entity.drift.inventory.slot.gun;

import org.forweb.drift.entity.drift.PolygonalObjectEntity;
import org.forweb.drift.entity.drift.inventory.Inventory;
import org.forweb.drift.entity.drift.inventory.gun.energy.MinorEnergyGun;
import org.forweb.drift.entity.drift.spaceships.PolygonalSpaceShip;

public class MinorEnergySlot extends GunSlot {
    public MinorEnergySlot(PolygonalObjectEntity configuration) {
        super(configuration);
    }

    @Override
    protected boolean canMount(Inventory inventory) {
        return inventory instanceof MinorEnergyGun;
    }

}
