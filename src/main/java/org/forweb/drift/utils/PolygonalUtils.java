package org.forweb.drift.utils;

import org.forweb.drift.entity.drift.PolygonalObject;
import org.forweb.geometry.misc.Angle;
import org.forweb.geometry.misc.Vector;
import org.forweb.geometry.services.LineService;
import org.forweb.geometry.services.PointService;
import org.forweb.geometry.shapes.Circle;
import org.forweb.geometry.shapes.Line;
import org.forweb.geometry.shapes.Point;

public class PolygonalUtils {

    private static Point zero = new Point(0, 0);

    public static Point[] translatePoints(Point[] points, double x, double y, Angle angle, boolean appendLastPoint) {
        int length = points.length + (points.length > 2 && appendLastPoint ? 1: 0);
        Point[] out = new Point[length];
        for(int i = 0; i < points.length; i++) {
            Point p = PointService.translate(zero, points[i], angle);
            out[i] = new Point(p.getX() + x, p.getY() + y);
        }
        if(appendLastPoint && points.length > 2) {
            out[points.length] = out[0];
        }
        return out;
    }

    public static boolean hasIntersections(PolygonalObject object, PolygonalObject object1) {

        if (Math.pow(object1.getRadius() - object.getRadius(), 2) <=
                Math.pow((object1.getX() - object.getX()), 2) + Math.pow(object1.getY() - object.getY(), 2)
                ) {
            return false;
        }
        Point[] thisPoints = object1.getPoints();
        if (thisPoints == null) {
            return false;
        }
        Point[] baseThisPoints = object1.getPoints();
        thisPoints = PolygonalUtils.translatePoints(
                baseThisPoints,
                object1.getX(),
                object1.getY(),
                object1.getAngle(),
                true
        );
        int thisLength = baseThisPoints.length;
        try {
            Point[] baseThatPoints = object.getPoints();
            if (baseThatPoints == null) {
                return false;
            }
            Point[] thatPoints = PolygonalUtils.translatePoints(
                    object.getPoints(),
                    object.getX(),
                    object.getY(),
                    object.getAngle(),
                    true
            );

            int thatLength = baseThatPoints.length;
            if (thatLength <= 1) {
                return false;
            }

            while (--thisLength > 0) {
                Line line1 = new Line(thisPoints[thisLength], thisPoints[thisLength - 1]);
                thatLength = thatPoints.length;
                while (--thatLength > 0) {
                    Line line2 = new Line(thatPoints[thatLength], thatPoints[thatLength - 1]);
                    if (LineService.lineHasIntersections(line1, line2)) {
                        return true;
                    }
                }
            }
        } catch (NullPointerException e) {
            throw e;
        }
        return false;
    }

    public static void applyForceToPoint(PolygonalObject object, Vector force, Point point) {
        double x = object.getX();
        double y = object.getY();
        double radius = object.getRadius();
        if(PointService.pointBelongToCircle(point, new Circle(x, y, radius)) >= 0) {

            double a = force.y,
                   b = -force.x,
                   c = force.x * point.getY() - force.y * point.getX();
            double distance = Math.abs(a*object.getX() + b*object.getY() + c)/Math.sqrt(a * a + b * b);

            //let's think that if power applied to max radius, half will be used for rotation, half for movement
            double rotationScale = distance / radius;
            double movementScale = 1 - rotationScale;
            applyForceToCenter(object, new Vector(force.x * movementScale, force.y * movementScale));

            Point pointOfApplication = new Point(point.getX() - x, point.getY() - y);
            double torque = pointOfApplication.getX() * force.y - pointOfApplication.getY() * force.x;
            double angle;
            angle = (Math.PI * rotationScale / object.getMass());
            if(torque < 0) {
                angle = -angle;
            }
            object.getRotation().append(angle);
        }

    }

    public static void applyForceToCenter(PolygonalObject object, Vector force) {
        double power = Math.sqrt(force.x * force.x + force.y * force.y);
        applyForce(object, force, power);
    }

    private static void applyForce(PolygonalObject object, Vector force, double power) {
        double scale = power / object.getMass();
        object.getVector().append(new Vector(force.x * scale, force.y * scale));
    }

    public static Point[] translatePoints(Point[] points, double x, double y, Angle angle) {
        Point[] out = new Point[points.length];
        for(int i = 0; i < points.length; i++) {
            Point p = PointService.translate(zero, points[i], angle);
            out[i] = new Point(p.getX() + x, p.getY() + y);
        }
        return out;
    }
}
