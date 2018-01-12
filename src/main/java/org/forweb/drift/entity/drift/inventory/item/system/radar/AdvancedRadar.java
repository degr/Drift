package org.forweb.drift.entity.drift.inventory.item.system.radar;

import org.forweb.drift.entity.drift.PolygonalObjectEntity;
import org.jbox2d.common.Vec2;

public class AdvancedRadar extends AbstractRadar {

    public AdvancedRadar(Vec2[] configuration) {
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
