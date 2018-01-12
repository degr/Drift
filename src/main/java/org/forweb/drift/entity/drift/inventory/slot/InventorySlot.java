package org.forweb.drift.entity.drift.inventory.slot;


import org.forweb.drift.entity.drift.PolygonalObjectEntity;
import org.forweb.drift.entity.drift.inventory.item.Inventory;
import org.forweb.drift.entity.drift.spaceships.PolygonalSpaceShip;
import org.jbox2d.dynamics.Body;

public abstract class InventorySlot {

    private final PolygonalSpaceShip spaceShip;
    private boolean active;
    private double angle;
    private double x;
    private double y;


    private Inventory inventory;

    public InventorySlot(PolygonalObjectEntity configuration, PolygonalSpaceShip spaceShip) {
        this.x = configuration.getX();
        this.y = configuration.getY();
        this.angle = configuration.getAngle();
        this.spaceShip = spaceShip;
    }


    protected abstract boolean canMount(Inventory inventory);

    public abstract void command(String command);

    public abstract void affect(PolygonalSpaceShip spaceShip);

    public Inventory mount(Inventory inventory) {
        if (this.canMount(inventory)) {
            if (this.inventory == null) {
                this.inventory = inventory;
                applySlotConfiguration();
                this.inventory.mount(spaceShip);
                return null;
            } else {
                Inventory out = this.inventory;
                out.unMount(spaceShip);
                this.inventory = inventory;
                applySlotConfiguration();
                this.inventory.mount(spaceShip);
                return out;
            }
        } else {
            return inventory;
        }
    }

    protected void applySlotConfiguration() {
    }


    public double getAngle() {
        return angle;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public PolygonalSpaceShip getSpaceShip() {
        return spaceShip;
    }
}
