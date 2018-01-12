package org.forweb.drift.entity.drift.inventory.item.system.radar;

import org.jbox2d.common.Vec2;

public class MinorRadar extends AbstractRadar {
    public MinorRadar() {
        super(new Vec2[]{new Vec2(0, 0), new Vec2(0.5, 0.7), new Vec2(0.5, -0.7)}, 5);
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
