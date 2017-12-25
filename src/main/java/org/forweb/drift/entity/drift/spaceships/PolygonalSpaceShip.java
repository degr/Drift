package org.forweb.drift.entity.drift.spaceships;

import org.forweb.drift.entity.drift.PolygonalObject;
import org.forweb.drift.entity.drift.PolygonalObjectEntity;
import org.forweb.drift.entity.drift.inventory.Cargo;
import org.forweb.drift.entity.drift.inventory.Inventory;
import org.forweb.drift.entity.drift.inventory.InventorySlot;
import org.forweb.drift.utils.InventoryUtils;
import org.forweb.geometry.misc.Angle;
import org.forweb.geometry.services.PointService;
import org.forweb.geometry.shapes.Point;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;


public abstract class PolygonalSpaceShip extends PolygonalObject {

    private Cargo cargo;
    private List<InventorySlot> slots;

    public PolygonalSpaceShip(PolygonalObjectEntity configuration) {
        super(configuration);
        cargo = new Cargo(configuration.getCapacity());
        slots = new ArrayList<>();
    }

    protected void mountInventory(InventorySlot slot, Inventory inventory) {
        InventoryUtils.mount(this, slot, inventory);
    }

    protected void addInventory(InventorySlot inventory) {
        slots.add(inventory);
    }

    public Inventory addCargo(Inventory inventory) {
        if (!cargo.put(inventory)) {
            return inventory;
        } else {
            return null;
        }
    }

    @Override
    public void update() {
        super.update();
        for (InventorySlot slot : slots) {
            if (slot.isActive()) {
                slot.affect(this);
            }
        }
    }


    public void draw(Graphics g) {
        super.draw(g);
        for (InventorySlot slot : slots) {
            Inventory inventory = slot.getInventory();
            if (inventory != null) {
                double x = inventory.getX();
                double y = inventory.getY();
                Angle angle = inventory.getAngle();
                Point result = PointService.translate(new Point(0, 0), new Point(x, y), getAngle());
                inventory.setX(result.getX() + getX());
                inventory.setY(result.getY() + getY());
                inventory.setAngle(angle.sum(getAngle().doubleValue()));
                inventory.draw(g);
                inventory.setX(x);
                inventory.setY(y);
                inventory.setAngle(angle);
            }
        }
    }

    public abstract void command(String slot, String command);

}
