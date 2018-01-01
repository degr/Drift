package org.forweb.drift.entity.drift.inventory.slot.engine;

import org.forweb.drift.entity.drift.PolygonalObjectEntity;
import org.forweb.drift.entity.drift.inventory.item.Inventory;
import org.forweb.drift.entity.drift.inventory.item.engine.ShuntingEngine;
import org.forweb.drift.entity.drift.spaceships.PolygonalSpaceShip;
import org.forweb.geometry.misc.Angle;
import org.forweb.geometry.misc.Vector;

public class ShuntingEngineSlot extends EngineSlot {

    private static final int NOT_STOP_ROTATION = 0;
    private static final int STOP_ROTATION = 1;

    private int stabilisation;

    public ShuntingEngineSlot(PolygonalObjectEntity configuration, PolygonalSpaceShip spaceShip) {
        super(configuration, spaceShip);
    }

    @Override
    protected boolean canMount(Inventory inventory) {
        return inventory instanceof ShuntingEngine;
    }


    @Override
    public void command(String command) {
        String[] parts = command.split(":");
        super.command(parts[0]);
        if(parts.length > 1 && "s".equals(parts[1])) {
            setStabilisation(STOP_ROTATION);
        } else {
            setStabilisation(NOT_STOP_ROTATION);
        }
    }


    protected void postAffect(PolygonalSpaceShip spaceShip) {
        int stabilisation = getStabilisation();
        double scale;
        double rotationAngle;
        switch (stabilisation) {
            case STOP_ROTATION:
                double rotation = spaceShip.getRotation().doubleValue();
                if(Math.abs(rotation) < 0.00001) {
                    this.setActive(false);
                    return;
                } else {
                    if ((Math.PI - rotation > 0 && this.rotationAngle < 0) ||
                            Math.PI - rotation < 0 && this.rotationAngle > 0) {
                        if (Math.abs(rotation) - Math.abs(this.rotationAngle) < 0) {
                            scale = Math.abs(rotation) / Math.abs(this.rotationAngle);
                            rotationAngle = -rotation;
                        } else {
                            scale = 1;
                            rotationAngle = this.rotationAngle;
                        }
                    } else {
                        this.setActive(false);
                        return;
                    }
                }
                break;
            default:
                scale = 1;
                rotationAngle = this.rotationAngle;
        }

        Inventory inventory = getInventory();
        Angle angle = inventory.getAngle().sum(spaceShip.getAngle().doubleValue());
        spaceShip.applyForceToCenter(new Vector(vectorLength * scale * angle.cos(), vectorLength * scale * angle.sin()));
        spaceShip.getRotation().append(rotationAngle);
    }


    public int getStabilisation() {
        return stabilisation;
    }

    public void setStabilisation(int stabilisation) {
        this.stabilisation = stabilisation;
    }

}
