package org.forweb.drift.entity.drift.inventory.item.engine;

import org.forweb.drift.entity.drift.PolygonalObject;
import org.forweb.drift.entity.drift.PolygonalObjectEntity;
import org.forweb.drift.entity.drift.inventory.item.Inventory;
import org.forweb.drift.entity.drift.spaceships.PolygonalSpaceShip;
import org.forweb.geometry.shapes.Point;

public abstract class Engine extends Inventory{
    public Engine(PolygonalObjectEntity configuration, double bulk) {
        super(configuration, bulk);
    }

    public abstract double getPower();


    @Override
    public void mount(PolygonalSpaceShip spaceShip) {

    }

    @Override
    public void unMount(PolygonalSpaceShip spaceShip) {

    }

    @Override
    public PolygonalObject[] generate() {
        return null;
    }

    @Override
    public PolygonalObject[] onImpact(PolygonalObject that, Point impact) {
        return null;
    }
}
