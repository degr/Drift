package org.forweb.drift.entity.drift;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.forweb.drift.utils.IncrementalId;
import org.forweb.geometry.misc.Vector;

public class Gun extends BaseObject{

    private Vector vector;
    private boolean reload;
    @JsonIgnore
    private long lastFireTime;

    public Gun(double x, double y, int id) {
        this(x, y, 0, id);
    }
    public Gun(double x, double y, double angle, int id) {
        super(x, y, angle, id);
        vector = new Vector(0, 0);
        this.reload = false;
        lastFireTime = 0;
    }

    public boolean canFire() {
        if(!reload) {
            return true;
        } else {
            if(System.currentTimeMillis() - 1000 > lastFireTime) {
                reload = false;
            }
            return !reload;
        }
    }

    public Bullet fire(IncrementalId ids) {
        reload = true;
        lastFireTime = System.currentTimeMillis();
        return new Bullet(getX(), getY(), getAngle().doubleValue(), ids.get());
    }


    @Override
    public String getType() {
        return Types.gun.toString();
    }

    @JsonIgnore
    @Override
    public boolean isRelativePoints() {
        return false;
    }

    public Vector getVector() {
        return vector;
    }

    public void setVector(Vector vector) {
        this.vector = vector;
    }

    public boolean isReload() {
        return reload;
    }

    public void setReload(boolean reload) {
        this.reload = reload;
    }

    @JsonIgnore
    public long getLastFireTime() {
        return lastFireTime;
    }

    public void setLastFireTime(long lastFireTime) {
        this.lastFireTime = lastFireTime;
    }
}