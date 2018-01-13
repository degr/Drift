package org.forweb.drift.entity.drift;

import org.forweb.drift.utils.AsteroidUtils;
import org.forweb.geometry.misc.Angle;
import org.forweb.geometry.misc.Vector;
import org.forweb.geometry.shapes.Point;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.World;

import java.util.Random;

public class PolygonalAsteroid extends PolygonalObject {


    private boolean alive;

    /**
     * Used from asteroid factory
     * @param x position
     * @param y position
     */
    public PolygonalAsteroid(World world, double x, double y) {
        this(world, new PolygonalObjectEntity(AsteroidUtils.createPoints(), x, y, 0));
    }

    public PolygonalAsteroid(World world, PolygonalObjectEntity configuration) {
        super(world, configuration);
        Random random = new Random();
        Body body = getBody();
        body.setLinearVelocity(new Vec2(random.nextDouble() * 0.2 - 0.1, random.nextDouble() * 0.2 - 0.1));
        body.setAngularVelocity(Math.random() * 0.02 - 0.01);
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


    public void destroy() {
        setAlive(false);
        AsteroidUtils.onImpact(this);
    }



    public void setAlive(boolean alive) {
        this.alive = alive;
    }
}
