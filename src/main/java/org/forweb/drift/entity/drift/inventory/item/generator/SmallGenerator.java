package org.forweb.drift.entity.drift.inventory.item.generator;

import org.jbox2d.common.Vec2;

public class SmallGenerator extends AbstractGenerator {
    public SmallGenerator(Vec2[] configuration) {
        super(configuration, 7);
    }

    private int step = 0;
    @Override
    public int getEnergyProduction() {
        if(step == 2) {
            step = 0;
            return 1 ;
        } else {
            step++;
            return 0;
        }
    }

}
