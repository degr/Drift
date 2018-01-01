package org.forweb.drift.utils;

import org.forweb.drift.entity.drift.inventory.slot.InventorySlot;
import org.forweb.drift.entity.drift.spaceships.PolygonalSpaceShip;
import org.forweb.drift.entity.drift.inventory.item.Inventory;

public class InventoryUtils {
    public static void mount(PolygonalSpaceShip polygonalSpaceShip, InventorySlot slot, Inventory inventory) {
        Inventory cargo = slot.mount(inventory);
        if(cargo != null) {
            polygonalSpaceShip.addCargo(cargo);
        }
    }
}
