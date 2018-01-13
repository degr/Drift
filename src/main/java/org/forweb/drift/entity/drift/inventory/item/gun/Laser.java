package org.forweb.drift.entity.drift.inventory.item.gun;

import org.forweb.drift.entity.drift.PolygonalObjectEntity;
import org.forweb.drift.entity.drift.inventory.item.ammo.Ammo;
import org.forweb.drift.entity.drift.inventory.item.ammo.LaserRay;
import org.forweb.drift.entity.drift.inventory.item.gun.energy.MinorEnergyGun;
import org.forweb.geometry.shapes.Point;
import org.jbox2d.common.Vec2;

public class Laser extends MinorEnergyGun {
    public Laser() {
        super(new Vec2[]{
                new Vec2(-1, 0.5), new Vec2(1, 0.5),
                new Vec2(1, -0.5), new Vec2(-1, -0.5)
        });
    }

    @Override
    public double getDistance() {
        return 400;
    }

    @Override
    public long getFireRate() {
        return 500;
    }

    @Override
    public Ammo fire() {
        return new LaserRay();
    }

    @Override
    public int getEnergyConsumption() {
        return 70;
    }
}
