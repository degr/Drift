package org.forweb.drift.entity.drift;


import org.forweb.drift.utils.IncrementalId;
import org.forweb.drift.utils.MassUtils;
import org.forweb.drift.utils.PolygonalUtils;
import org.forweb.geometry.misc.Angle;
import org.forweb.geometry.misc.Vector;
import org.forweb.geometry.services.PointService;
import org.forweb.geometry.shapes.Point;

import java.awt.*;
import java.util.concurrent.atomic.AtomicInteger;

public class PolygonalObject {

    private static final AtomicInteger idGenerator = new AtomicInteger(0);

    private double y;
    private double x;
    private Point[] points;
    private Angle angle;
    private Angle rotation;
    private double radius;
    private Vector vector;

    private double mass;
    private int id;

    public PolygonalObject(PolygonalObjectEntity configuration) {
        setAngle(new Angle(configuration.getAngle()));
        setPoints(configuration.getPoints());
        setX(configuration.getX());
        setY(configuration.getY());
        setVector(new Vector(0, 0));
        setRotation(new Angle(0));
        id = idGenerator.incrementAndGet();
    }

    private void initRadius() {
        radius = 0;
        for (Point p : getPoints()) {
            double d = Math.sqrt(p.getX() * p.getX() + p.getY() * p.getY());
            if (d > radius) {
                radius = d;
            }
        }
    }

    public Point[] getPoints() {
        return translatePoints(points, getX(), getY(), getAngle());
    }

    public Point[] getRelativePoints() {
        return points;
    }


    private static Point[] translatePoints(Point[] points, double x, double y, Angle angle) {
        return PolygonalUtils.translatePoints(points, x, y, angle);
    }

    public Angle getAngle() {
        return angle;
    }

    public void setPoints(Point[] points) {
        this.points = MassUtils.setToCenter(points);
        mass = MassUtils.getSquare(this.points);
        this.initRadius();
    }

    public void setAngle(Angle angle) {
        this.angle = angle;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getMass() {
        return mass;
    }

    public boolean hasImpacts(PolygonalObject object) {
        return PolygonalUtils.hasIntersections(this, object);
    }

    public void applyForceToPoint(Vector force, Point point) {
        PolygonalUtils.applyForceToPoint(this, force, point);
    }

    public void applyForceToCenter(Vector force) {
        PolygonalUtils.applyForceToCenter(this, force);
    }

    public Vector getVector() {
        return vector;
    }

    public void setVector(Vector vector) {
        this.vector = vector;
    }

    public double getRadius() {
        return radius;
    }
    public Angle getRotation() {
        return rotation;
    }

    public void setRotation(Angle rotation) {
        this.rotation = rotation;
    }

    public void update() {
        Vector v = getVector();
        setX(getX() + v.x);
        setY(getY() + v.y);
        getAngle().append(getRotation());
    }


    public void draw(Graphics g) {
        Point[] points = getPoints();
        for (int i = 0; i < points.length; i++) {
            Point a = points[i];
            Point b = i == points.length - 1 ? points[0] : points[i + 1];
            g.drawLine((int) a.getX(), (int) a.getY(), (int) b.getX(), (int) b.getY());
        }
    }

    public int getId() {
        return id;
    }

}
