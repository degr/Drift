package org.forweb.drift.utils;

public class IncrementalId {
    private int counter;

    public IncrementalId() {
        counter = 1;
    }

    public int get() {
        if(counter > 99999) {
            counter = 1;
        }
        return counter++;
    }
}
