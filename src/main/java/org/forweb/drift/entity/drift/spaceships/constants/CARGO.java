package org.forweb.drift.entity.drift.spaceships.constants;

public enum CARGO {

    FALCON(300);

    public final int amount;

    private CARGO(int amount) {
        this.amount = amount;
    }
}
