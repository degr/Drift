package org.forweb.drift.entity.drift.inventory.slot.gun;

import org.forweb.drift.entity.drift.PolygonalObjectEntity;
import org.forweb.drift.entity.drift.inventory.Inventory;

public class HeavyGunSlot extends GunSlot{

    public HeavyGunSlot(PolygonalObjectEntity configuration) {
        super(configuration);
    }

    @Override
    protected boolean canMount(Inventory inventory) {
        return false;
    }

}
