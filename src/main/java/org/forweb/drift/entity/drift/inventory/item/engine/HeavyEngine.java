package org.forweb.drift.entity.drift.inventory.item.engine;

import org.forweb.drift.entity.drift.PolygonalObjectEntity;
import org.forweb.drift.entity.drift.inventory.constants.ENERGY;
import org.forweb.geometry.shapes.Point;

public class HeavyEngine extends Engine{

    public HeavyEngine() {
        super(new PolygonalObjectEntity(new Point[]{
                new Point(-2, 3), new Point(1, 2),
                new Point(1, -2), new Point(-2, -3)
        }, 0, 0, 0), 10);
    }

    @Override
    public double getPower() {
        return 7;
    }

    @Override
    public int getEnergyConsumption() {
        return ENERGY.ENGINE_HEAVY.amount;
    }
}
