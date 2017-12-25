package org.forweb.drift.entity.drift;

import org.forweb.geometry.shapes.Point;

public class PolygonalObjectEntity {

    public PolygonalObjectEntity(double x, double y, double angle) {
        this(null, x, y, angle);
    }

    public PolygonalObjectEntity(Point[] points, double x, double y, double angle) {
        this(points, x, y, angle, 0);
    }

    public PolygonalObjectEntity(Point[] points, double x, double y, double angle, double capacity) {
        this.points = points;
        this.x = x;
        this.y = y;
        this.angle = angle;
        this.capacity = capacity;
    }

    private Point[] points;
    private double x;
    private double y;
    private double angle;
    private double capacity;

    public Point[] getPoints() {
        return points;
    }

    public void setPoints(Point[] points) {
        this.points = points;
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

    public double getAngle() {
        return angle;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    public double getCapacity() {
        return capacity;
    }

    public void setCapacity(double capacity) {
        this.capacity = capacity;
    }

}
