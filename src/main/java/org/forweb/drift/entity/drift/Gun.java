package org.forweb.drift.entity.drift;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.forweb.drift.utils.IncrementalId;
import org.forweb.geometry.misc.Vector;

public class Gun extends BaseObject{

    private Vector vector;
    private String color;
    private boolean reload;
    private long lastFireTime;

    public Gun(double x, double y, int id) {
        this(x, y, 0, "red", id);
    }
    public Gun(double x, double y, double angle, String color, int id) {
        super(x, y, angle, id);
        vector = new Vector(0, 0);
        this.color = color;
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
        return "gun";
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

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public boolean isReload() {
        return reload;
    }

    public void setReload(boolean reload) {
        this.reload = reload;
    }

    public long getLastFireTime() {
        return lastFireTime;
    }

    public void setLastFireTime(long lastFireTime) {
        this.lastFireTime = lastFireTime;
    }
}