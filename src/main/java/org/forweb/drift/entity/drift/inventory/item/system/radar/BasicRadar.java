package org.forweb.drift.entity.drift.inventory.item.system.radar;

import org.jbox2d.common.Vec2;

public class BasicRadar extends AbstractRadar {
    public BasicRadar(Vec2[] configuration) {
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
