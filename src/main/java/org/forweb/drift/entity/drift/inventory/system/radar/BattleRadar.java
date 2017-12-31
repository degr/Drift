package org.forweb.drift.entity.drift.inventory.system.radar;

import org.forweb.drift.entity.drift.PolygonalObjectEntity;
import org.forweb.drift.entity.drift.inventory.system.AbstractRadar;

public class BattleRadar extends AbstractRadar {

    public BattleRadar(PolygonalObjectEntity configuration) {
        super(configuration, 12);
    }

    @Override
    public double getRadius() {
        return 400;
    }

    @Override
    public boolean canScan() {
        return true;
    }

    @Override
    public int getEnergyConsumption() {
        return 2;
    }
}
