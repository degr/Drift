package org.forweb.drift.entity.drift.inventory.generator;

import org.forweb.drift.entity.drift.PolygonalObjectEntity;
import org.forweb.drift.entity.drift.spaceships.PolygonalSpaceShip;

public class SmallGenerator extends AbstractGenerator {
    public SmallGenerator(PolygonalObjectEntity configuration) {
        super(configuration, 7);
    }

    @Override
    public void affect(PolygonalSpaceShip spaceShip) {
        spaceShip.addEnergy(2);
    }
}
