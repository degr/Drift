package org.forweb.drift.entity.drift.inventory.item.system;

import org.forweb.drift.entity.drift.PolygonalObjectEntity;
import org.forweb.drift.entity.drift.spaceships.PolygonalSpaceShip;
import org.jbox2d.common.Vec2;

public class SolarBattery extends InternalSystem {
    private int i;

    public SolarBattery(Vec2[] configuration) {
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

    @Override
    public void mount(PolygonalSpaceShip spaceShip) {

    }

    @Override
    public void unMount(PolygonalSpaceShip spaceShip) {

    }
}
