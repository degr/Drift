package org.forweb.drift.entity.drift.inventory.item.generator;

import org.forweb.drift.entity.drift.PolygonalObjectEntity;
import org.forweb.drift.entity.drift.spaceships.PolygonalSpaceShip;

public class SmallGenerator extends AbstractGenerator {
    public SmallGenerator(PolygonalObjectEntity configuration) {
        super(configuration, 7);
    }

    @Override
    public int getEnergyProduction() {
        return 3;
    }

}
