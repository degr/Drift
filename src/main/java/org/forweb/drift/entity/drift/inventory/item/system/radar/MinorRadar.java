package org.forweb.drift.entity.drift.inventory.item.system.radar;

import org.forweb.drift.entity.drift.PolygonalObjectEntity;
import org.forweb.geometry.shapes.Point;

public class MinorRadar extends AbstractRadar {
    public MinorRadar() {
        super(new PolygonalObjectEntity(
                new Point[]{new Point(0, 0), new Point(0.5, 0.7), new Point(0.5, -0.7)}, 0, 0, 0
        ), 5);
    }

    @Override
    public double getRadius() {
        return 250;
    }

    @Override
    public boolean canScan() {
        return false;
    }

    @Override
    public int getEnergyConsumption() {
        return 1;
    }
}
