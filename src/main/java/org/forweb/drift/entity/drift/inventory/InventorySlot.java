package org.forweb.drift.entity.drift.inventory;


import org.forweb.drift.entity.drift.PolygonalObjectEntity;
import org.forweb.drift.entity.drift.spaceships.PolygonalSpaceShip;
import org.forweb.geometry.misc.Angle;
import org.springframework.security.access.method.P;

import javax.persistence.criteria.CriteriaBuilder;

public abstract class InventorySlot {

    private final PolygonalSpaceShip spaceShip;
    private boolean active;
    private Angle angle;
    private double x;
    private double y;


    private Inventory inventory;

    public InventorySlot(PolygonalObjectEntity configuration, PolygonalSpaceShip spaceShip) {
        this.x = configuration.getX();
        this.y = configuration.getY();
        this.angle = new Angle(configuration.getAngle());
        this.spaceShip = spaceShip;
    }


    protected abstract boolean canMount(Inventory inventory);
    public abstract void command(String command);
    public abstract void affect(PolygonalSpaceShip spaceShip);

    public Inventory mount(Inventory inventory) {
        if(this.canMount(inventory)) {
            if(this.inventory == null) {
                this.inventory = inventory;
                applyConfiguration();
                return null;
            } else {
                Inventory out = this.inventory;
                this.inventory = inventory;
                applyConfiguration();
                return out;
            }
        } else {
            return inventory;
        }
    }

    protected void applyConfiguration() {
        this.inventory.setX(getX());
        this.inventory.setY(getY());
        this.inventory.setAngle(getAngle());
    }


    public Angle getAngle() {
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
