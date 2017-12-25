package org.forweb.drift.entity.drift.inventory.slot.engine;

import org.forweb.drift.entity.drift.PolygonalObjectEntity;
import org.forweb.drift.entity.drift.inventory.Inventory;
import org.forweb.drift.entity.drift.inventory.InventorySlot;
import org.forweb.drift.entity.drift.inventory.engine.Engine;
import org.forweb.drift.entity.drift.spaceships.PolygonalSpaceShip;
import org.forweb.geometry.misc.Angle;
import org.forweb.geometry.misc.Vector;
import org.forweb.geometry.services.PointService;
import org.forweb.geometry.shapes.Point;

public abstract class EngineSlot extends InventorySlot {

    public EngineSlot(PolygonalObjectEntity configuration) {
        super(configuration);
    }

    @Override
    public void command(String command) {
        setActive("1".equals(command));
    }


    @Override
    public void affect(PolygonalSpaceShip spaceShip) {
        Inventory inventory = getInventory();
        if(inventory != null) {
            Engine engine = (Engine)inventory;
            double power = engine.getPower();
            Angle vectorAngle = spaceShip.getAngle().sum(getAngle().doubleValue());
            Vector vector = new Vector(power * vectorAngle.cos(), power * vectorAngle.sin());
            Point translatedPoint = PointService.translate(
                    new Point(0, 0),
                    new Point(this.getX(), this.getY()),
                    spaceShip.getAngle()
            );
            spaceShip.applyForceToPoint(
                    vector,
                    new Point(translatedPoint.getX() + spaceShip.getX(), translatedPoint.getY() + spaceShip.getY())
            );
        }
    }

}
