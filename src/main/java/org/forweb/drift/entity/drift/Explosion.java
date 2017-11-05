package org.forweb.drift.entity.drift;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.forweb.geometry.misc.Vector;

public class Explosion extends BaseObject {

    private int life;
    private double radius;

    public Explosion(double x, double y, Vector vector, double radius, int id) {
        this(x, y, vector, radius, 100, id);
    }
    public Explosion(double x, double y, Vector vector, double radius, int life, int id) {
        super(x, y, 0, id);
        setVector(vector);
        this.radius = radius;
        this.life = life;
    }

    @Override
    public String getType() {
        return "explosion";
    }

    @JsonIgnore
    @Override
    public boolean isRelativePoints() {
        return false;
    }

    public void update() {
        this.life--;
        Vector vector = getVector();
        setX(getX() + vector.x);
        setY(getY() + vector.y);
    }

    @Override
    public boolean hasImpact(BaseObject baseObject) {
        return false;
    }

    public boolean isAlive() {
        return life > 0;
    }
    public double getRadius() {
        return radius;
    }
}