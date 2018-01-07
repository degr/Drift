package org.forweb.drift.entity.drift;

import org.forweb.drift.utils.AsteroidUtils;
import org.forweb.geometry.misc.Angle;
import org.forweb.geometry.misc.Vector;
import org.forweb.geometry.shapes.Point;

import java.util.Random;

public class PolygonalAsteroid extends PolygonalObject {


    private boolean alive;

    /**
     * Used from asteroid factory
     * @param x position
     * @param y position
     */
    public PolygonalAsteroid(double x, double y) {
        this(new PolygonalObjectEntity(AsteroidUtils.createPoints(), x, y, 0));
    }

    public PolygonalAsteroid(PolygonalObjectEntity configuration) {
        super(configuration);
        Random random = new Random();
        setVector(new Vector(random.nextDouble() * 2 - 1, random.nextDouble() * 2 - 1));
        this.setRotation(new Angle(Math.random() * 0.2 - 0.1));
        this.alive = true;
    }

    @Override
    public PolygonalObject[] generate() {
        return null;
    }

    @Override
    public PolygonalObject[] onImpact(PolygonalObject that, Point impact) {
        super.onImpact(that, impact);
        return null;
    }
    @Override
    public boolean isAlive() {
        return this.alive;
    }

    @Override
    public boolean isInvincible() {
        return false;
    }

    public Point hasImpact(PolygonalObject baseObject) {
        if (baseObject instanceof PolygonalAsteroid) {
            return null;
        } else {
            return super.hasImpact(baseObject);
        }
    }

    public PolygonalObject[] onImpact(BaseObject object) {
        return AsteroidUtils.onImpact(this, object);
    }



    public void setAlive(boolean alive) {
        this.alive = alive;
    }
}
