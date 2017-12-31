package org.forweb.drift.entity.drift.inventory.gun;

import org.forweb.drift.entity.drift.PolygonalObjectEntity;
import org.forweb.drift.entity.drift.inventory.ammo.Ammo;
import org.forweb.drift.entity.drift.inventory.gun.energy.MinorEnergyGun;
import org.forweb.geometry.shapes.Point;

public class Laser extends MinorEnergyGun {
    public Laser() {
        super(new PolygonalObjectEntity(new Point[]{
                new Point(-1, 0.5), new Point(1, 0.5),
                new Point(1, -0.5), new Point(-1, -0.5)
        }, 0, 0, 0));
    }

    @Override
    public Ammo fire() {
        return null;
    }

    @Override
    public int getEnergyConsumption() {
        return 3;
    }
}
