package org.forweb.drift.entity.drift.inventory.constants;

public enum ENERGY {

    ENGINE_SHUNTING(3), ENGINE_BASIC(8), ENGINE_HEAVY(20);

    public final int amount;

    private ENERGY(int amount) {
        this.amount = amount;
    }

}
