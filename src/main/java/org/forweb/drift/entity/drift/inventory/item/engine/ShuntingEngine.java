package org.forweb.drift.entity.drift.inventory.item.engine;

import org.forweb.drift.entity.drift.PolygonalObjectEntity;
import org.forweb.drift.entity.drift.inventory.constants.ENERGY;
import org.forweb.geometry.shapes.Point;
import org.jbox2d.common.Vec2;

public class ShuntingEngine extends Engine{

    public ShuntingEngine() {
        super(new Vec2[]{
                new Vec2(-1, 1), new Vec2(1, 1),
                new Vec2(1, -1), new Vec2(-1, -1)
        }, 2);
    }

    @Override
    public double getPower() {
        return 0.4;
    }

    @Override
    public int getEnergyConsumption() {
        return ENERGY.ENGINE_SHUNTING.amount;
    }
}
