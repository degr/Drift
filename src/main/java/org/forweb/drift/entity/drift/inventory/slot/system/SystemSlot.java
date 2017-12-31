package org.forweb.drift.entity.drift.inventory.slot.system;

import org.forweb.drift.entity.drift.PolygonalObjectEntity;
import org.forweb.drift.entity.drift.inventory.Inventory;
import org.forweb.drift.entity.drift.inventory.InventorySlot;
import org.forweb.drift.entity.drift.inventory.generator.AbstractGenerator;
import org.forweb.drift.entity.drift.inventory.system.InternalSystem;
import org.forweb.drift.entity.drift.spaceships.PolygonalSpaceShip;

public class SystemSlot extends InventorySlot{
    public SystemSlot(PolygonalObjectEntity configuration, PolygonalSpaceShip spaceShip) {
        super(configuration, spaceShip);
    }

    @Override
    protected boolean canMount(Inventory inventory) {
        return inventory instanceof InternalSystem && !(inventory instanceof AbstractGenerator);
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
