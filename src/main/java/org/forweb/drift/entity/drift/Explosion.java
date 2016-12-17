package org.forweb.drift.entity.drift;

import org.forweb.geometry.misc.Vector;

public class Explosion extends BaseObject {

    private int life;
    private double radius;

    public Explosion(double x, double y, Vector vector, double radius) {
        this(x, y, vector, radius, 100);
    }
    public Explosion(double x, double y, Vector vector, double radius, int life) {
        super(x, y, 0);
        setVector(vector);
        this.radius = radius;
        this.life = life;
    }

    public void update() {
        this.life--;
        Vector vector = getVector();
        setX(getX() + vector.x);
        setY(getY() + vector.y);
    }

    public boolean isAlive() {
        return life > 0;
    }
}