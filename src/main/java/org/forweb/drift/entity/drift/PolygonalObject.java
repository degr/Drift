package org.forweb.drift.entity.drift;


import org.forweb.geometry.shapes.Point;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;

import java.awt.*;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class PolygonalObject {

    private static final AtomicInteger idGenerator = new AtomicInteger(0);


    private int id;
    private Body body;

    public PolygonalObject(World world, PolygonalObjectEntity configuration) {
        BodyDef def = new BodyDef();


        PolygonShape shape = new PolygonShape();
        shape.set(configuration.getPoints(), configuration.getPoints().length);

        FixtureDef fixture = new FixtureDef();
        fixture.shape = shape;
        fixture.density = 4.0f;
        fixture.friction = 0.2f;
        fixture.restitution = 0f;
        fixture.isSensor = false;


        def.type = BodyType.DYNAMIC;
        def.position = new Vec2(configuration.getX(), configuration.getY());
        def.angle = configuration.getAngle();
        def.linearVelocity = new Vec2(0, 0);
        def.angularVelocity = 0;
        def.linearDamping = 0;
        def.angularDamping = 0;
        def.allowSleep = false;
        def.awake = true;
        def.fixedRotation = false;
        def.bullet = false;
        def.active = true;
        def.gravityScale = 1;

        body = world.createBody(def);
        body.createFixture(fixture);
        body.setUserData(this);
        id = idGenerator.incrementAndGet();

    }



    public abstract boolean isAlive();
    public abstract boolean isInvincible();
    public abstract PolygonalObject[] generate();
    public PolygonalObject[] onImpact(PolygonalObject that, Point impact) {

        return null;
    }


    public Vec2[] getRelativePoints() {
        Body body = getBody();
        Fixture fixture = body.getFixtureList();
        org.jbox2d.collision.shapes.Shape shape = fixture.getShape();
        PolygonShape polygon = (PolygonShape) shape;
        Vec2[] points = polygon.getVertices();
        int count = polygon.getVertexCount();
        Vec2[] out = new Vec2[count];
        System.arraycopy(points, 0, out, 0, count);
        return out;
    }

    public void update() {
    }

    public void draw(Graphics g) {
        Vec2[] points = getRelativePoints();
        int scale = 3;
        for (int i = 0; i < points.length; i++) {
            Vec2 a = points[i];
            Vec2 b = i == points.length - 1 ? points[0] : points[i + 1];
            g.drawLine((int) a.x * scale, (int) a.y * scale, (int) b.x * scale, (int) b.y * scale);
        }
    }

    public int getId() {
        return id;
    }

    public Body getBody() {
        return body;
    }
}
