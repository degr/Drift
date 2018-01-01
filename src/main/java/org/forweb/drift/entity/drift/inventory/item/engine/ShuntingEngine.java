package org.forweb.drift.entity.drift.inventory.item.engine;

import org.forweb.drift.entity.drift.PolygonalObjectEntity;
import org.forweb.drift.entity.drift.inventory.constants.ENERGY;
import org.forweb.geometry.shapes.Point;

public class ShuntingEngine extends Engine{

    public ShuntingEngine() {
        super(new PolygonalObjectEntity(new Point[]{
                new Point(-1, 1), new Point(1, 1),
                new Point(1, -1), new Point(-1, -1)
        }, 0, 0, 0), 2);
    }

    @Override
    public double getPower() {
        return 1.3;
    }

    @Override
    public int getEnergyConsumption() {
        return ENERGY.ENGINE_SHUNTING.amount;
    }
}
