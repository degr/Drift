package org.forweb.drift.entity.drift.inventory.item.system;

import org.forweb.drift.entity.drift.PolygonalObjectEntity;
import org.forweb.drift.entity.drift.spaceships.PolygonalSpaceShip;
import org.forweb.geometry.shapes.Point;

public class BasicRepairSystem extends InternalSystem {
    public BasicRepairSystem() {
        super(new PolygonalObjectEntity(
                new Point[]{
                        new Point(-0.5, 0.5),new Point(0.5, 0.5),new Point(0.5, -0.5),new Point(-0.5, -0.5)
                }, 0, 0, 0),
                10
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
