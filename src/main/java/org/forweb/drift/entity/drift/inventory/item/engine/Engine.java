package org.forweb.drift.entity.drift.inventory.item.engine;

import org.forweb.drift.entity.drift.PolygonalObject;
import org.forweb.drift.entity.drift.PolygonalObjectEntity;
import org.forweb.drift.entity.drift.inventory.item.Inventory;
import org.forweb.drift.entity.drift.spaceships.PolygonalSpaceShip;
import org.forweb.geometry.shapes.Point;
import org.jbox2d.common.Vec2;

public abstract class Engine extends Inventory{
    public Engine(Vec2[] points, double bulk) {
        super(points, bulk);
    }

    public abstract double getPower();


    @Override
    public void mount(PolygonalSpaceShip spaceShip) {

    }

    @Override
    public void unMount(PolygonalSpaceShip spaceShip) {

    }

}
