package org.forweb.drift.entity.drift.inventory.item.system.radar;

import org.forweb.drift.entity.drift.PolygonalObjectEntity;
import org.jbox2d.common.Vec2;

public class BattleRadar extends AbstractRadar {

    public BattleRadar(Vec2[] configuration) {
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
