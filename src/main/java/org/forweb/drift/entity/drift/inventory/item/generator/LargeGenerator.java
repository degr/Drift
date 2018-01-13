package org.forweb.drift.entity.drift.inventory.item.generator;

import org.jbox2d.common.Vec2;

public class LargeGenerator extends AbstractGenerator {
    public LargeGenerator(Vec2[] configuration) {
        super(configuration, 20);
    }

    @Override
    public int getEnergyProduction() {
        return 4;
    }

}
