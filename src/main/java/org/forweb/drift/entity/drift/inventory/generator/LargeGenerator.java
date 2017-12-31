package org.forweb.drift.entity.drift.inventory.generator;

import org.forweb.drift.entity.drift.PolygonalObjectEntity;
import org.forweb.drift.entity.drift.spaceships.PolygonalSpaceShip;

public class LargeGenerator extends AbstractGenerator {
    public LargeGenerator(PolygonalObjectEntity configuration) {
        super(configuration, 20);
    }

    @Override
    public void affect(PolygonalSpaceShip spaceShip) {
        spaceShip.addEnergy(7);
    }
}
