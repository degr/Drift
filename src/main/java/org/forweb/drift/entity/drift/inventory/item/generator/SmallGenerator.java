package org.forweb.drift.entity.drift.inventory.item.generator;

import org.jbox2d.common.Vec2;

public class SmallGenerator extends AbstractGenerator {
    public SmallGenerator(Vec2[] configuration) {
        super(configuration, 7);
    }

    @Override
    public int getEnergyProduction() {
        return 3;
    }

}
