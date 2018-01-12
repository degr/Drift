package org.forweb.drift.entity.drift.inventory.slot.engine;

import org.forweb.drift.entity.drift.PolygonalObjectEntity;
import org.forweb.drift.entity.drift.inventory.item.Inventory;
import org.forweb.drift.entity.drift.inventory.item.engine.ShuntingEngine;
import org.forweb.drift.entity.drift.spaceships.PolygonalSpaceShip;
import org.forweb.geometry.misc.Angle;
import org.forweb.geometry.misc.Vector;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;

public class ShuntingEngineSlot extends EngineSlot {

    private static final int NOT_STOP_ROTATION = 0;
    private static final int STOP_ROTATION = 1;


    public ShuntingEngineSlot(PolygonalObjectEntity configuration, PolygonalSpaceShip spaceShip) {
        super(configuration, spaceShip);
    }

    @Override
    protected boolean canMount(Inventory inventory) {
        return inventory instanceof ShuntingEngine;
    }


}
