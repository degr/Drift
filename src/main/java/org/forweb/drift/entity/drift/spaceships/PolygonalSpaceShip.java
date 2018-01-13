package org.forweb.drift.entity.drift.spaceships;

import org.forweb.drift.entity.drift.PolygonalObject;
import org.forweb.drift.entity.drift.PolygonalObjectEntity;
import org.forweb.drift.entity.drift.inventory.Cargo;
import org.forweb.drift.entity.drift.inventory.item.Inventory;
import org.forweb.drift.entity.drift.inventory.slot.InventorySlot;
import org.forweb.drift.utils.InventoryUtils;
import org.forweb.geometry.shapes.Point;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.World;

import java.util.ArrayList;
import java.util.List;


public abstract class PolygonalSpaceShip extends PolygonalObject {

    private Cargo cargo;
    private List<InventorySlot> slots;
    private int energy;
    private int maxEnergy;
    private int energyRegeneration;


    private boolean alive;
    private boolean invincible;
    private int maxHealth;

    public PolygonalSpaceShip(World world, PolygonalObjectEntity configuration, int maxEnergy, int energyRegeneration) {
        super(world, configuration);
        cargo = new Cargo(configuration.getCapacity());
        slots = new ArrayList<>();
        setAlive(true);
        setMaxHealth(getHealth());
        setMaxEnergy(maxEnergy);
        setEnergy(maxEnergy);
        setEnergyRegeneration(energyRegeneration);
    }

    protected void mountInventory(InventorySlot slot, Inventory inventory) {
        InventoryUtils.mount(this, slot, inventory);
    }

    protected void addInventorySlot(InventorySlot inventorySlot) {
        slots.add(inventorySlot);
    }
    public InventorySlot getSlot(int position) {
        if(slots.size() >= position) {
            return slots.get(position);
        } else {
            return null;
        }
    }

    public void command(int slotPosition, String command) {
        InventorySlot slot = getSlot(slotPosition);
        if(slot != null) {
            slot.command(command);
        }
    }

    public Inventory addCargo(Inventory inventory) {
        if (!cargo.put(inventory)) {
            return inventory;
        } else {
            return null;
        }
    }

    public boolean useEnergy(int amount) {
        int current = getEnergy();
        if(current > amount) {
            this.setEnergy(current - amount);
            return true;
        } else {
            return false;
        }
    }


    @Override
    public boolean isAlive() {
        return alive;
    }

    @Override
    public boolean isInvincible() {
        return invincible;
    }

    @Override
    public PolygonalObject[] generate() {
        return null;
    }

    @Override
    public PolygonalObject[] onImpact(PolygonalObject that, Point impact) {
        super.onImpact(that, impact);
        System.out.println("Impact");
        return null;
    }

    public void addHealth(int health) {
        int current = getHealth();
        int max = getMaxHealth();
        int amount;
        if (current + health > max) {
            amount = max - current;
        } else {
            amount = health;
        }
        setHealth(current + amount);
    }
    public void addEnergy(int energy) {
        int current = getEnergy();
        int max = getMaxEnergy();
        int amount;
        if (current + energy > max) {
            amount = max - current;
        } else {
            amount = energy;
        }
        setEnergy(current + amount);
    }

    public void setEnergy(int energy) {
        this.energy = energy;
    }

    public int getEnergy() {
        return energy;
    }

    public int getMaxEnergy() {
        return maxEnergy;
    }

    public void setMaxEnergy(int maxEnergy) {
        this.maxEnergy = maxEnergy;
    }

    public Cargo getCargo() {
        return cargo;
    }

    public void setEnergyRegeneration(int energyRegeneration) {
        this.energyRegeneration = energyRegeneration;
    }

    public int getEnergyRegeneration() {
        return energyRegeneration;
    }


    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public void setInvincible(boolean invincible) {
        this.invincible = invincible;
    }

    @Override
    public void update() {
        super.update();
        addEnergy(getEnergyRegeneration());
        addHealth(getHealthRegeneration());
        for(InventorySlot slot : slots) {
            if(slot.isActive()) {
                slot.affect(this);
            }
        }
    }

    protected abstract int getHealthRegeneration();

    public void setMaxHealth(int maxHealth) {
        this.maxHealth = maxHealth;
    }

    public int getMaxHealth() {
        return maxHealth;
    }
}
