package org.forweb.drift.entity.drift.inventory.slot.engine;

import org.forweb.drift.entity.drift.PolygonalObjectEntity;
import org.forweb.drift.entity.drift.inventory.Inventory;
import org.forweb.drift.entity.drift.inventory.InventorySlot;
import org.forweb.drift.entity.drift.inventory.engine.Engine;
import org.forweb.drift.entity.drift.spaceships.PolygonalSpaceShip;
import org.forweb.drift.utils.PolygonalUtils;
import org.forweb.geometry.misc.Angle;
import org.forweb.geometry.misc.Vector;
import org.forweb.geometry.services.PointService;
import org.forweb.geometry.shapes.Point;

public abstract class EngineSlot extends InventorySlot {

    private double cachedMass;
    private double rotationAngle;
    private double vectorLength;

    public EngineSlot(PolygonalObjectEntity configuration) {
        super(configuration);
        cachedMass = 0;
    }

    @Override
    public void command(String command) {
        setActive("1".equals(command));
    }

    @Override
    protected void applyConfiguration() {

    }

    @Override
    public void affect(PolygonalSpaceShip spaceShip) {
        Inventory inventory = getInventory();
        if(inventory != null) {
            if(cachedMass == 0 || cachedMass != spaceShip.getMass()) {
                Engine engine = (Engine)inventory;
                double power = engine.getPower();
                Angle vectorAngle = spaceShip.getAngle().sum(getAngle().doubleValue());
                Vector vector = new Vector(power * vectorAngle.cos(), power * vectorAngle.sin());
                Point translatedPoint = PointService.translate(
                        new Point(0, 0),
                        new Point(this.getX(), this.getY()),
                        spaceShip.getAngle()
                );

                Point engineForcePoint = new Point(
                        translatedPoint.getX() + spaceShip.getX(),
                        translatedPoint.getY() + spaceShip.getY()
                );
                rotationAngle = PolygonalUtils.calculateAngle(spaceShip, vector, engineForcePoint);
                vector = PolygonalUtils.calculateVector(spaceShip, vector, engineForcePoint);
                vectorLength = Math.sqrt(vector.x * vector.x + vector.y * vector.y);
                cachedMass = spaceShip.getMass();
            }
            Angle angle = inventory.getAngle().sum(spaceShip.getAngle().doubleValue());
            spaceShip.applyForceToCenter(new Vector(vectorLength * angle.cos(), vectorLength * angle.sin()));
            spaceShip.getRotation().append(this.rotationAngle);
        }
    }

}
