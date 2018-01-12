package org.forweb.drift.entity.drift.inventory.item.generator;

import org.forweb.drift.entity.drift.PolygonalObjectEntity;
import org.forweb.drift.entity.drift.spaceships.PolygonalSpaceShip;
import org.forweb.geometry.shapes.Point;
import org.jbox2d.common.Vec2;

public class BasicGenerator extends AbstractGenerator {
    public BasicGenerator() {
        super(new Vec2[]{
                new Vec2(-1, 1), new Vec2(1, 1), new Vec2(1, -1), new Vec2(-1, -1),
        }, 10);
    }

    @Override
    public int getEnergyProduction() {
        return 5;
    }
}
