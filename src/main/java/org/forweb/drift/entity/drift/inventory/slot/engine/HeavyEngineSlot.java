package org.forweb.drift.entity.drift.inventory.slot.engine;

import org.forweb.drift.entity.drift.PolygonalObjectEntity;
import org.forweb.drift.entity.drift.inventory.Inventory;
import org.forweb.drift.entity.drift.inventory.engine.BasicEngine;
import org.forweb.drift.entity.drift.inventory.engine.HeavyEngine;
import org.forweb.drift.entity.drift.spaceships.PolygonalSpaceShip;

public class HeavyEngineSlot extends EngineSlot {

    public HeavyEngineSlot(PolygonalObjectEntity configuration, PolygonalSpaceShip spaceShip) {
        super(configuration, spaceShip);
    }

    @Override
    protected boolean canMount(Inventory inventory) {
        return inventory instanceof HeavyEngine || inventory instanceof BasicEngine;
    }

}
