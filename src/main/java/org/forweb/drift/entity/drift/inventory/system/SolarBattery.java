package org.forweb.drift.entity.drift.inventory.system;

import org.forweb.drift.entity.drift.PolygonalObjectEntity;
import org.forweb.drift.entity.drift.spaceships.PolygonalSpaceShip;

public class SolarBattery extends InternalSystem {
    private int i;

    public SolarBattery(PolygonalObjectEntity configuration) {
        super(configuration, 5);
        this.i = 0;
    }


    @Override
    public void affect(PolygonalSpaceShip spaceShip) {
        i++;
        if(i > 10) {
            spaceShip.addEnergy(1);
            i = 0;
        }
    }

    @Override
    public int getEnergyConsumption() {
        return 0;
    }
}
