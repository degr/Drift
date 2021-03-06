package org.forweb.drift.entity.drift.spaceships.constants;

public enum ENERGY {
    FALCON(400, 2);

    public final int amount;
    public final int regeneration;

    private ENERGY(int amount, int regeneration) {
        this.amount = amount;
        this.regeneration = regeneration;
    }
}
