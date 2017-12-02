package org.forweb.drift.entity.drift.controlable.base;

import org.forweb.geometry.misc.Vector;

public class LandingOperation {
    private boolean interrupted;
    private long ts;
    private Vector landingVector;

    public LandingOperation() {
        updateTs();
    }

    public boolean interrupted() {
        return interrupted && System.currentTimeMillis() - ts < 3000;
    }

    public void interrupted(boolean interrupted) {
        this.interrupted = interrupted;
        ts = System.currentTimeMillis();
        landingVector = null;
    }

    public boolean expire() {
        return System.currentTimeMillis() - ts > 30000;
    }

    public Vector getLandingVector() {
        return landingVector;
    }

    public void setLandingVector(Vector landingVector) {
        this.landingVector = landingVector;
    }

    public void updateTs() {
        ts = System.currentTimeMillis();
    }
}
