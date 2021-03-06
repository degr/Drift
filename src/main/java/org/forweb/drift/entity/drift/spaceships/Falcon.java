package org.forweb.drift.entity.drift.spaceships;

import org.forweb.drift.entity.drift.PolygonalObjectEntity;
import org.forweb.drift.entity.drift.inventory.slot.engine.MainEngineSlot;
import org.forweb.drift.entity.drift.inventory.slot.engine.ShuntingEngineSlot;
import org.forweb.drift.entity.drift.inventory.slot.gun.MinorEnergySlot;
import org.forweb.drift.entity.drift.inventory.slot.system.SystemSlot;
import org.forweb.drift.entity.drift.spaceships.constants.CARGO;
import org.forweb.drift.entity.drift.spaceships.constants.ENERGY;
import org.forweb.drift.entity.drift.spaceships.constants.POINTS;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;

public class Falcon extends PolygonalSpaceShip {

    public Falcon(World world, double x, double y) {
        super(world, new PolygonalObjectEntity(
                        POINTS.FALCON.points,
                        x,
                        y,
                        0d,
                        CARGO.FALCON.amount
                ),
                ENERGY.FALCON.amount,
                ENERGY.FALCON.regeneration
        );
        Vec2[] points = this.getRelativePoints();
        addInventorySlot(new MainEngineSlot(
                new PolygonalObjectEntity(points[0].x, 0, 0),
                this
        ));//0
        addInventorySlot(new ShuntingEngineSlot(
                new PolygonalObjectEntity(points[2].x - 5, 3.5, - Math.PI * 3.3 / 4),
                this
        ));//1
        addInventorySlot(new ShuntingEngineSlot(
                new PolygonalObjectEntity(points[2].x - 5, -3.5, Math.PI * 3.3 / 4),
                this
        ));//2
        addInventorySlot(new SystemSlot(
                new PolygonalObjectEntity(points[2].x - 24, points[2].y, 0),
                this
        ));//3
        addInventorySlot(new SystemSlot(
                new PolygonalObjectEntity(points[0].x + 7, points[0].y + 5, 0),
                this
        ));//4
        addInventorySlot(new SystemSlot(
                new PolygonalObjectEntity(points[1].x + 7, points[1].y - 5, 0),
                this
        ));//5
        addInventorySlot(new MinorEnergySlot(
                new PolygonalObjectEntity(points[2].x + 5, points[2].y + 3, 0),
                this
        ));//6
        addInventorySlot(new MinorEnergySlot(
                new PolygonalObjectEntity(points[1].x + 5, points[1].y - 3, 0),
                this
        ));//7
    }

    @Override
    public void destroy() {
        setAlive(false);
    }

    private int healthScale = 0;
    @Override
    protected int getHealthRegeneration() {
        if(healthScale < 10) {
            healthScale++;
            return 0;
        } else {
            healthScale = 0;
            return 1;
        }
    }
}
