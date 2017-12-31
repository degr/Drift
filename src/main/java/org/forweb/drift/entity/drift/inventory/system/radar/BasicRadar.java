package org.forweb.drift.entity.drift.inventory.system.radar;

import org.forweb.drift.entity.drift.PolygonalObjectEntity;
import org.forweb.drift.entity.drift.inventory.system.AbstractRadar;

public class BasicRadar extends AbstractRadar {
    public BasicRadar(PolygonalObjectEntity configuration) {
        super(configuration, 10);
    }

    @Override
    public double getRadius() {
        return 400;
    }

    @Override
    public boolean canScan() {
        return false;
    }

    @Override
    public int getEnergyConsumption() {
        return 2;
    }
}
