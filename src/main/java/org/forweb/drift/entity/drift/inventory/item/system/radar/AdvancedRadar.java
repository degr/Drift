package org.forweb.drift.entity.drift.inventory.item.system.radar;

import org.forweb.drift.entity.drift.PolygonalObjectEntity;

public class AdvancedRadar extends AbstractRadar {

    public AdvancedRadar(PolygonalObjectEntity configuration) {
        super(configuration, 25);
    }

    @Override
    public double getRadius() {
        return 800;
    }

    @Override
    public boolean canScan() {
        return true;
    }

    @Override
    public int getEnergyConsumption() {
        return 3;
    }

}
