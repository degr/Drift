package org.forweb.drift.entity.drift.inventory.slot.engine;

import org.forweb.drift.entity.drift.PolygonalObjectEntity;
import org.forweb.drift.entity.drift.inventory.item.Inventory;
import org.forweb.drift.entity.drift.inventory.slot.InventorySlot;
import org.forweb.drift.entity.drift.inventory.item.engine.Engine;
import org.forweb.drift.entity.drift.spaceships.PolygonalSpaceShip;
import org.forweb.drift.utils.PolygonalUtils;
import org.forweb.geometry.misc.Angle;
import org.forweb.geometry.misc.Vector;
import org.forweb.geometry.services.PointService;
import org.forweb.geometry.shapes.Point;

public abstract class EngineSlot extends InventorySlot {

    protected double cachedMass;
    protected double rotationAngle;
    protected double vectorLength;

    public EngineSlot(PolygonalObjectEntity configuration, PolygonalSpaceShip spaceShip) {
        super(configuration, spaceShip);
        cachedMass = 0;
    }

    @Override
    public void command(String command) {
        setActive("1".equals(command));
    }

    @Override
    protected void applySlotConfiguration() {
        Inventory inventory = getInventory();
        inventory.setX(getX());
        inventory.setY(getY());
        inventory.setAngle(getAngle());
    }

    @Override
    public void affect(PolygonalSpaceShip spaceShip) {
        Inventory inventory = getInventory();
        if(inventory != null) {
            if(!spaceShip.useEnergy(inventory.getEnergyConsumption())) {
                return;
            }
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
                double rotationScale = PolygonalUtils.calculateRotationScale(spaceShip, vector, engineForcePoint);
                rotationAngle = PolygonalUtils.calculateAngle(spaceShip, vector, engineForcePoint, rotationScale);
                vector = PolygonalUtils.calculateVector(spaceShip, vector, rotationScale);
                vectorLength = Math.sqrt(vector.x * vector.x + vector.y * vector.y);
                cachedMass = spaceShip.getMass();
            }
            postAffect(spaceShip);
        }
    }

    protected void postAffect(PolygonalSpaceShip spaceShip) {
        Inventory inventory = getInventory();
        Angle angle = inventory.getAngle().sum(spaceShip.getAngle().doubleValue());
        spaceShip.applyForceToCenter(new Vector(vectorLength * angle.cos(), vectorLength * angle.sin()));
        spaceShip.getRotation().append(this.rotationAngle);
    }


}
