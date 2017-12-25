package org.forweb.drift.entity.drift.spaceships;

import org.forweb.drift.entity.drift.PolygonalObjectEntity;
import org.forweb.drift.entity.drift.inventory.Inventory;
import org.forweb.drift.entity.drift.inventory.engine.Engine;
import org.forweb.drift.entity.drift.inventory.gun.Gun;
import org.forweb.drift.entity.drift.inventory.slot.engine.MainEngineSlot;
import org.forweb.drift.entity.drift.inventory.slot.engine.ShuntingEngineSlot;
import org.forweb.drift.entity.drift.inventory.slot.gun.MinorEnergySlot;
import org.forweb.geometry.shapes.Point;

public class Falcon extends PolygonalSpaceShip {

    private MainEngineSlot mainEngineSlot;
    private ShuntingEngineSlot shuntingLeftSlot;
    private ShuntingEngineSlot shuntingRightSlot;
    private MinorEnergySlot gunSlot;

    public Falcon(double x, double y) {
        super(new PolygonalObjectEntity(new Point[]{new Point(-10d, -10d), new Point(-10d, 10d), new Point(20, 0)}, x, y, 0d, 300));
        Point[] points = this.getRelativePoints();
        mainEngineSlot = new MainEngineSlot(new PolygonalObjectEntity(points[0].getX(), 0, 0));
        shuntingLeftSlot = new ShuntingEngineSlot(new PolygonalObjectEntity(points[2].getX() - 5, -5, Math.PI / 4));
        shuntingRightSlot = new ShuntingEngineSlot(new PolygonalObjectEntity(points[2].getX() - 5, 5, Math.PI * 7 / 8));
        gunSlot = new MinorEnergySlot(new PolygonalObjectEntity(points[2].getX(), 0, 0));

        addInventory(mainEngineSlot);
        addInventory(shuntingLeftSlot);
        addInventory(shuntingRightSlot);
        addInventory(gunSlot);
    }


    public void command(String slot, String command) {
        switch (slot) {
            case "e":
                mainEngineSlot.command(command);
                break;
            case "sl":
                shuntingLeftSlot.command(command);
                break;
            case "sr":
                shuntingRightSlot.command(command);
                break;
            case "f":
                gunSlot.command(command);
                break;
        }
    }

    public Inventory setMainEngine(Engine engine) {
        return mainEngineSlot.mount(engine);
    }
    public Inventory setShuntingLeftEngine(Engine engine) {
        return shuntingLeftSlot.mount(engine);
    }
    public Inventory setShuntingRightEngine(Engine engine) {
        return shuntingRightSlot.mount(engine);
    }
    public Inventory setGun(Gun gun) {
        return gunSlot.mount(gun);
    }
}
