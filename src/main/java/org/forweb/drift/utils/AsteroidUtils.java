package org.forweb.drift.utils;

import org.forweb.drift.entity.drift.BaseObject;
import org.forweb.drift.entity.drift.PolygonalAsteroid;
import org.forweb.drift.entity.drift.PolygonalObject;
import org.forweb.drift.entity.drift.PolygonalObjectEntity;
import org.forweb.geometry.shapes.Point;

import java.util.Random;

public class AsteroidUtils {

    public static PolygonalObject[] onImpact(PolygonalAsteroid asteroid, BaseObject object) {
        asteroid.setAlive(false);
        PolygonalObject[] out;
        if (asteroid.getPoints().length > 3) {
            out = createNewAsteroids(asteroid);
        } else {
            out = new PolygonalObject[1];
        }
        return out;
    }


    private static PolygonalObject[] createNewAsteroids(PolygonalAsteroid parent) {
        double y = parent.getY();
        double x = parent.getX();
        Point[] points = parent.getRelativePoints();
        int firstSize = 3;
        int start = 0;
        double angle = parent.getAngle().doubleValue();
        double totalSquare = MassUtils.getSquare(parent.getPoints());

        while (true) {
            Point[] p1 = new Point[firstSize];
            Point[] p2 = new Point[points.length - firstSize + 2];
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
                return processByTwoPoints(p1, p2, x, y, angle);
            } else {
                start++;
            }
        }
        //return new BaseObject[1];
    }

    private static PolygonalAsteroid[] processByTwoPoints(Point[] p1, Point[] p2, double x, double y, double angle) {
        PolygonalAsteroid[] out = new PolygonalAsteroid[3];
        double[] aCenter = MassUtils.getCenterOfMass(p1[0], p1[1], p1[2]);
        p1 = MassUtils.setToCenter(p1, aCenter);
        out[0] = new PolygonalAsteroid(new PolygonalObjectEntity(p1, x + aCenter[0], y + aCenter[1], angle));

        aCenter = MassUtils.getCenterOfMass(p2);
        p2 = MassUtils.setToCenter(p2, aCenter);
        out[1] = new PolygonalAsteroid(new PolygonalObjectEntity(p2, x + aCenter[0], y + aCenter[1], angle));
        return out;
    }

    public static Point[] createPoints() {
        Random random = new Random();
        int limit = (int) Math.ceil(random.nextDouble() * 7) + 3;
        double sector = Math.PI * 2 / limit;
        Point[] points = new Point[limit];
        while (limit-- > 0) {
            double angle = (Math.random() * (sector)) + sector * limit;
            double distance = Math.random() * 90;
            points[limit] = new Point(
                    Math.cos(angle) * distance,
                    Math.sin(angle) * distance
            );
        }
        return MassUtils.setToCenter(points);
    }
}
