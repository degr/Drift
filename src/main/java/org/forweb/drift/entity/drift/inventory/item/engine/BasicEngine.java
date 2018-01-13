package org.forweb.drift.entity.drift.inventory.item.engine;

import org.forweb.drift.entity.drift.PolygonalObjectEntity;
import org.forweb.drift.entity.drift.inventory.constants.ENERGY;
import org.forweb.drift.entity.drift.spaceships.PolygonalSpaceShip;
import org.forweb.geometry.shapes.Point;
import org.jbox2d.common.Vec2;

public class BasicEngine extends Engine{

    public BasicEngine() {
        super(new Vec2[]{
                new Vec2(-2, 2), new Vec2(1, 1),
                new Vec2(1, -1), new Vec2(-2, -2)
        }, 5);
    }

    @Override
    public double getPower() {
        return 1;
    }

    @Override
    public int getEnergyConsumption() {
        return ENERGY.ENGINE_BASIC.amount;
    }
}
