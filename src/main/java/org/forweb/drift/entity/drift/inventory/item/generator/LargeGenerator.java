package org.forweb.drift.entity.drift.inventory.item.generator;

import org.forweb.drift.entity.drift.PolygonalObjectEntity;
import org.forweb.drift.entity.drift.spaceships.PolygonalSpaceShip;

public class LargeGenerator extends AbstractGenerator {
    public LargeGenerator(PolygonalObjectEntity configuration) {
        super(configuration, 20);
    }

    @Override
    public int getEnergyProduction() {
        return 12;
    }

}
