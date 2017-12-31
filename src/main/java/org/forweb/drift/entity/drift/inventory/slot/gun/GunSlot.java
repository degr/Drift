package org.forweb.drift.entity.drift.inventory.slot.gun;

import org.forweb.drift.entity.drift.PolygonalObjectEntity;
import org.forweb.drift.entity.drift.inventory.InventorySlot;
import org.forweb.drift.entity.drift.spaceships.PolygonalSpaceShip;

public abstract class GunSlot extends InventorySlot {

    public GunSlot(PolygonalObjectEntity configuration, PolygonalSpaceShip spaceShip) {
        super(configuration, spaceShip);
    }

    @Override
    public void command(String command) {
        this.setActive("1".equals(command));
    }


    @Override
    public void affect(PolygonalSpaceShip spaceShip) {

    }
}
