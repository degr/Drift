package org.forweb.drift.entity.drift.inventory.item.system;

import org.forweb.drift.entity.drift.spaceships.PolygonalSpaceShip;
import org.forweb.geometry.shapes.Point;
import org.jbox2d.common.Vec2;

public class BasicRepairSystem extends InternalSystem {
    public BasicRepairSystem() {
        super(new Vec2[]{
                        new Vec2(-0.5, 0.5), new Vec2(0.5, 0.5), new Vec2(0.5, -0.5), new Vec2(-0.5, -0.5)

                }, 10
        );
    }

    @Override
    public int getEnergyConsumption() {
        return 3;
    }

    @Override
    public void affect(PolygonalSpaceShip spaceShip) {

    }

    @Override
    public void mount(PolygonalSpaceShip spaceShip) {

    }

    @Override
    public void unMount(PolygonalSpaceShip spaceShip) {

    }
}
