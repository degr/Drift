package org.forweb.drift.entity.drift.inventory;

import org.forweb.drift.entity.drift.inventory.item.Inventory;

import java.util.ArrayList;
import java.util.List;

public class Cargo {
    private double bulk;
    private double allowedBulk;

    private List<Inventory> inventory;

    public Cargo(double allowedBulk) {
        bulk = 0;
        this.allowedBulk = allowedBulk;
        inventory = new ArrayList<>();
    }

    public boolean put(Inventory inventory) {
        if(inventory.getBulk() + bulk <= allowedBulk) {
            this.inventory.add(inventory);
            return true;
        } else {
            return false;
        }
    }

    public Inventory get(int id) {
        for (Inventory inventory : this.inventory) {
            if(inventory.getId() == id) {
                return inventory;
            }
        }
        return null;
    }

    public void setAllowedBulk(double allowedBulk) {
        this.allowedBulk = allowedBulk;
    }

    public double getAllowedBulk() {
        return allowedBulk;
    }
}
