package org.forweb.drift.entity.drift.inventory.slot.engine;

import org.forweb.drift.entity.drift.PolygonalObjectEntity;
import org.forweb.drift.entity.drift.inventory.Inventory;
import org.forweb.drift.entity.drift.inventory.engine.ShuntingEngine;

public class ShuntingEngineSlot extends EngineSlot {

    public ShuntingEngineSlot(PolygonalObjectEntity configuration) {
        super(configuration);
    }

    @Override
    protected boolean canMount(Inventory inventory) {
        return inventory instanceof ShuntingEngine;
    }
}
