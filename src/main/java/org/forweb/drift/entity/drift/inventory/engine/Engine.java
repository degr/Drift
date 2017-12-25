package org.forweb.drift.entity.drift.inventory.engine;

import org.forweb.drift.entity.drift.PolygonalObjectEntity;
import org.forweb.drift.entity.drift.inventory.Inventory;
import org.forweb.geometry.misc.Vector;

public abstract class Engine extends Inventory{
    public Engine(PolygonalObjectEntity configuration, double bulk) {
        super(configuration, bulk);
    }

    public abstract double getPower();
}
