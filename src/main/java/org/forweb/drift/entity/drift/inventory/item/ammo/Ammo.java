package org.forweb.drift.entity.drift.inventory.item.ammo;

import org.forweb.drift.entity.drift.PolygonalObjectEntity;
import org.forweb.geometry.misc.Angle;

public abstract class Ammo {
    private double x;
    private double y;
    private double damage;
    private Angle angle;
    private boolean isInstant;

    public Ammo(PolygonalObjectEntity configuration, boolean isInstant) {
        x = configuration.getX();
        y = configuration.getY();
        angle = new Angle(configuration.getAngle());
        damage = configuration.getCapacity();
        this.isInstant = isInstant;
    }

    public void setDamage(double damage) {
        this.damage = damage;
    }
    public double getDamage() {
        return damage;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public Angle getAngle() {
        return angle;
    }

    public void setAngle(Angle angle) {
        this.angle = angle;
    }

    public boolean getIsInstant() {
        return isInstant;
    }

    public void setIsInstant(boolean isInstant) {
        this.isInstant = isInstant;
    }
}
