package org.forweb.drift.entity.drift.inventory.slot.system;

import org.forweb.drift.entity.drift.PolygonalObjectEntity;
import org.forweb.drift.entity.drift.inventory.Inventory;
import org.forweb.drift.entity.drift.inventory.InventorySlot;
import org.forweb.drift.entity.drift.inventory.generator.BasicGenerator;
import org.forweb.drift.entity.drift.inventory.generator.SmallGenerator;
import org.forweb.drift.entity.drift.spaceships.PolygonalSpaceShip;

public class GeneratorSlot extends InventorySlot {

    public GeneratorSlot(PolygonalObjectEntity configuration, PolygonalSpaceShip spaceShip) {
        super(configuration, spaceShip);
    }

    @Override
    protected boolean canMount(Inventory inventory) {
        return inventory instanceof SmallGenerator || inventory instanceof BasicGenerator;
    }

    @Override
    public void command(String command) {

    }

    @Override
    public void affect(PolygonalSpaceShip spaceShip) {

    }
}
