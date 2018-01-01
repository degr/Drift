package org.forweb.drift.entity.drift.inventory.slot.gun;

import org.forweb.drift.entity.drift.PolygonalObjectEntity;
import org.forweb.drift.entity.drift.inventory.item.Inventory;
import org.forweb.drift.entity.drift.inventory.item.gun.energy.MinorEnergyGun;
import org.forweb.drift.entity.drift.spaceships.PolygonalSpaceShip;

public class MinorEnergySlot extends GunSlot {
    public MinorEnergySlot(PolygonalObjectEntity configuration, PolygonalSpaceShip spaceShip) {
        super(configuration, spaceShip);
    }

    @Override
    protected boolean canMount(Inventory inventory) {
        return inventory instanceof MinorEnergyGun;
    }

}
