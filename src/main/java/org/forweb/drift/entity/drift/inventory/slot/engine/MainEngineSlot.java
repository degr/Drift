package org.forweb.drift.entity.drift.inventory.slot.engine;

import org.forweb.drift.entity.drift.PolygonalObjectEntity;
import org.forweb.drift.entity.drift.inventory.Inventory;
import org.forweb.drift.entity.drift.inventory.engine.BasicEngine;

public class MainEngineSlot extends EngineSlot {
    public MainEngineSlot(PolygonalObjectEntity configuration) {
        super(configuration);
    }

    @Override
    protected boolean canMount(Inventory inventory) {
        return inventory instanceof BasicEngine;
    }
}
