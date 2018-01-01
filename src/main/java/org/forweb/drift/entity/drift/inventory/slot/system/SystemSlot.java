package org.forweb.drift.entity.drift.inventory.slot.system;

import org.forweb.drift.entity.drift.PolygonalObjectEntity;
import org.forweb.drift.entity.drift.inventory.item.Inventory;
import org.forweb.drift.entity.drift.inventory.slot.InventorySlot;
import org.forweb.drift.entity.drift.inventory.item.generator.AbstractGenerator;
import org.forweb.drift.entity.drift.inventory.item.system.InternalSystem;
import org.forweb.drift.entity.drift.spaceships.PolygonalSpaceShip;

public class SystemSlot extends InventorySlot{
    public SystemSlot(PolygonalObjectEntity configuration, PolygonalSpaceShip spaceShip) {
        super(configuration, spaceShip);
    }

    @Override
    protected boolean canMount(Inventory inventory) {
        return inventory instanceof InternalSystem;
    }

    @Override
    public void command(String command) {
        Inventory inventory = getInventory();
        if(inventory != null) {

        }
    }

    @Override
    public void affect(PolygonalSpaceShip spaceShip) {

    }

}
