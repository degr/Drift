package org.forweb.drift.utils;

import org.forweb.drift.entity.drift.BaseObject;
import org.forweb.drift.entity.drift.PolygonalAsteroid;
import org.forweb.drift.entity.drift.PolygonalObject;
import org.forweb.drift.entity.drift.PolygonalObjectEntity;
import org.forweb.drift.entity.drift.inventory.slot.system.SystemSlot;
import org.forweb.geometry.shapes.Point;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.collision.shapes.Shape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.World;

import java.util.Random;

public class AsteroidUtils {

    public static PolygonalObject[] onImpact(World world, PolygonalAsteroid asteroid, BaseObject object) {
        asteroid.setAlive(false);
        PolygonalObject[] out;
        Body asteroidBody = null; /*asteroid.getBody();*/
        Shape shape = asteroidBody.getFixtureList().getShape();
        PolygonShape polygonShape = (PolygonShape)shape;
        if (polygonShape.getVertexCount() > 3) {
            out = createNewAsteroids(world, asteroidBody, polygonShape);
        } else {
            out = new PolygonalObject[1];
        }
        return out;
    }


    private static PolygonalObject[] createNewAsteroids(World world, Body asteroidBody, PolygonShape polygon) {
        double y = asteroidBody.getPosition().y;
        double x = asteroidBody.getPosition().x;
        Vec2[] points = normalizePoints(polygon.getVertices(), polygon.getVertexCount());
        int firstSize = 3;
        int start = 0;
        double angle = asteroidBody.getAngle();
        double totalSquare = MassUtils.getSquare(points);

        while (true) {
            Vec2[] p1 = new Vec2[firstSize];
            Vec2[] p2 = new Vec2[points.length - firstSize + 2];
            int i;
            int limit = firstSize + start;
            int j = 0;
            for (i = start; i < limit; i++) {
                if (i < points.length) {
                    p1[j] = points[i];
                } else {
                    p1[j] = points[i - points.length];
                }
                j++;
            }
            limit = points.length + start;
            for (i = start + 2; i <= limit; i++) {
                int index;
                if (i < points.length) {
                    index = i;
                } else {
                    index = i - points.length;
                }
                p2[i - start - 2] = points[index];
            }
            double square1 = MassUtils.getSquare(p1);
            double square2 = MassUtils.getSquare(p2);
            double check = totalSquare - square1 - square2;
            if (Math.abs(check) < 0.00001) {
                return processByTwoPoints(world, p1, p2, x, y, angle);
            } else {
                start++;
            }
        }
        //return new BaseObject[1];
    }

    private static Vec2[] normalizePoints(Vec2[] vertices, int vertexCount) {
        if(vertices.length == vertexCount) {
            return vertices;
        } else {
            Vec2[] out = new Vec2[vertexCount];
            System.arraycopy(vertices, 0, out, 0, vertices.length);
            return out;
        }
    }

    private static PolygonalAsteroid[] processByTwoPoints(World world, Vec2[] p1, Vec2[] p2, double x, double y, double angle) {
        PolygonalAsteroid[] out = new PolygonalAsteroid[3];
        double[] aCenter = MassUtils.getCenterOfMass(p1[0], p1[1], p1[2]);
        p1 = MassUtils.setToCenter(p1, aCenter);
        out[0] = new PolygonalAsteroid(world, new PolygonalObjectEntity(p1, x + aCenter[0], y + aCenter[1], angle));

        aCenter = MassUtils.getCenterOfMass(p2);
        p2 = MassUtils.setToCenter(p2, aCenter);
        out[1] = new PolygonalAsteroid(world, new PolygonalObjectEntity(p2, x + aCenter[0], y + aCenter[1], angle));
        return out;
    }

    public static Vec2[] createPoints() {
        Random random = new Random();
        int limit = (int) Math.ceil(random.nextDouble() * 7) + 3;
        double sector = Math.PI * 2 / limit;
        Vec2[] points = new Vec2[limit];
        while (limit-- > 0) {
            double angle = (Math.random() * (sector)) + sector * limit;
            double distance = Math.random() * 90;
            points[limit] = new Vec2(
                    Math.cos(angle) * distance,
                    Math.sin(angle) * distance
            );
        }
        return MassUtils.setToCenter(points);
    }
}
