package org.forweb.drift.entity.drift.inventory.constants;

public enum ENERGY {

    ENGINE_SHUNTING(7), ENGINE_BASIC(15), ENGINE_HEAVY(35);

    public final int amount;

    private ENERGY(int amount) {
        this.amount = amount;
    }

}
