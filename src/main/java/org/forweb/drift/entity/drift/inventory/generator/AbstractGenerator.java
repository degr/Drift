package org.forweb.drift.entity.drift.inventory.generator;

import org.forweb.drift.entity.drift.PolygonalObjectEntity;
import org.forweb.drift.entity.drift.inventory.system.InternalSystem;

public abstract class AbstractGenerator extends InternalSystem {
    public AbstractGenerator(PolygonalObjectEntity configuration, double bulk) {
        super(configuration, bulk);
    }

    @Override
    public int getEnergyConsumption() {
        return 0;
    }
}
