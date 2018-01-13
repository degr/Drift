package org.forweb.drift.entity.drift.inventory.item.ammo;

import org.forweb.drift.entity.drift.PolygonalObjectEntity;

public class LaserRay extends Ammo {
    public LaserRay() {
        super(new PolygonalObjectEntity(null, 0, 0, 0, 100), true);
    }


}
