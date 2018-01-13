package org.forweb.drift.entity.drift.inventory.slot.gun;

import org.forweb.drift.entity.drift.PolygonalObject;
import org.forweb.drift.entity.drift.PolygonalObjectEntity;
import org.forweb.drift.entity.drift.inventory.item.Inventory;
import org.forweb.drift.entity.drift.inventory.item.ammo.Ammo;
import org.forweb.drift.entity.drift.inventory.item.gun.Gun;
import org.forweb.drift.entity.drift.inventory.slot.InventorySlot;
import org.forweb.drift.entity.drift.spaceships.PolygonalSpaceShip;
import org.forweb.drift.utils.DriftWorld;
import org.jbox2d.collision.RayCastInput;
import org.jbox2d.collision.RayCastOutput;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.World;

public abstract class GunSlot extends InventorySlot {

    private long lastFire;

    public GunSlot(PolygonalObjectEntity configuration, PolygonalSpaceShip spaceShip) {
        super(configuration, spaceShip);
        lastFire = 0;
    }

    @Override
    public Inventory mount(Inventory inventory) {
        Inventory out = super.mount(inventory);
        if(out != inventory) {
            lastFire = 0;
            return out;
        } else {
            return inventory;
        }
    }

    @Override
    public void command(String command) {
        this.setActive("1".equals(command));
    }


    @Override
    public void affect(PolygonalSpaceShip spaceShip) {
        Inventory inventory = getInventory();
        if(inventory != null) {
            Gun gun = (Gun) inventory;
            if(System.currentTimeMillis() < lastFire + gun.getFireRate()) {
                return;
            }
            lastFire = System.currentTimeMillis();
            if(gun.isEnergy() && !spaceShip.useEnergy(gun.getEnergyConsumption())) {
                return;
            }
            Ammo ammo = gun.fire();
            if(ammo == null) {
                return;
            }
            Body spaceShipBody = spaceShip.getBody();
            RayCastInput laserRay = new RayCastInput();
            laserRay.maxFraction = 1;
            Vec2 point1 = spaceShipBody.getWorldPoint(new Vec2(getX(), getY()));
            laserRay.p1.set(point1);
            double length = gun.getDistance();
            double angle = spaceShipBody.getAngle() + getAngle();
            double oxShift = length * Math.cos(angle);
            double oyShift = length * Math.sin(angle);
            laserRay.p2.set(laserRay.p1.x + oxShift, laserRay.p1.y + oyShift);
            DriftWorld world = (DriftWorld)spaceShipBody.getWorld();
            Body body = world.getBodyList();
            world.addRay(laserRay);

            RayCastOutput output = new RayCastOutput();
            double fraction = 1;

            Body bodyUnderAttack = null;
            while(body != null) {
                Fixture fixture = body.getFixtureList();
                if(body != spaceShipBody) {
                    int child = 0;
                    while (fixture != null) {
                        if (fixture.raycast(output, laserRay, child) && output.fraction < fraction) {
                            fraction = output.fraction;
                            bodyUnderAttack = body;
                        }
                        child++;
                        fixture = fixture.getNext();
                    }
                }
                body = body.getNext();
            }
            if(bodyUnderAttack != null) {
                PolygonalObject object = (PolygonalObject) bodyUnderAttack.getUserData();
                object.setHealth(object.getHealth() - ammo.getDamage());
            }
        }
    }
}
