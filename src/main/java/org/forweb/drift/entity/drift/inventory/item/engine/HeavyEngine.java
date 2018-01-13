package org.forweb.drift.entity.drift.inventory.item.engine;

import org.forweb.drift.entity.drift.PolygonalObjectEntity;
import org.forweb.drift.entity.drift.inventory.constants.ENERGY;
import org.forweb.geometry.shapes.Point;
import org.jbox2d.common.Vec2;

public class HeavyEngine extends Engine{

    public HeavyEngine() {
        super(new Vec2[]{
                new Vec2(-2D, 3D), new Vec2(1, 2D),
                new Vec2(1, -2), new Vec2(-2, -3)
        }, 10D);
    }

    @Override
    public double getPower() {
        return 4;
    }

    @Override
    public int getEnergyConsumption() {
        return ENERGY.ENGINE_HEAVY.amount;
    }
}
