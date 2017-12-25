package org.forweb.drift.entity.drift.inventory.slot.engine;

import org.forweb.drift.entity.drift.PolygonalObjectEntity;
import org.forweb.drift.entity.drift.inventory.Inventory;
import org.forweb.drift.entity.drift.inventory.engine.BasicEngine;
import org.forweb.drift.entity.drift.inventory.engine.HeavyEngine;

public class HeavyEngineSlot extends EngineSlot {

    public HeavyEngineSlot(PolygonalObjectEntity configuration) {
        super(configuration);
    }

    @Override
    protected boolean canMount(Inventory inventory) {
        return inventory instanceof HeavyEngine || inventory instanceof BasicEngine;
    }

}
