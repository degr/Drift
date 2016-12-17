package org.forweb.drift.entity.drift;


import org.forweb.geometry.misc.Angle;
import org.forweb.geometry.misc.Vector;
import org.forweb.geometry.services.LineService;
import org.forweb.geometry.shapes.Line;
import org.forweb.geometry.shapes.Point;

public class BaseObject {
    private double x;
    private double y;
    private Angle angle;
    private Point[] points;
    private Vector vector;

    public BaseObject(double x, double y, double angle) {
        this(x, y, angle, null);
    }
    public BaseObject(double x, double y, double angle, Point[] points) {
        this.setX(x);
        this.setY(y);
        this.setAngle(new Angle(angle));
        this.setPoints(points);
    }

    public boolean hasImpact(BaseObject baseObject) {
        Point[] thisPoints = this.getPoints();
        int thisLength = thisPoints.length;
        Point[] thatPoints = baseObject.getPoints();
        int thatLength = thatPoints.length;
        while(--thisLength > 0) {
            while(--thatLength > 0) {
                Line line1 = new Line(thisPoints[thisLength], thisPoints[thisLength - 1]);
                Line line2 = new Line(thatPoints[thatLength], thatPoints[thatLength - 1]);
                if(LineService.lineHasIntersections(line1, line2)) {
                    return true;
                }
            }
        }
        return false;
    }

    public BaseObject[] onImpact(BaseObject object){
        return null;
    }

    public boolean isAlive() {
        return true;
    }

    //getters-setters
    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public Vector getVector() {
        return vector;
    }

    public void setVector(Vector vector) {
        this.vector = vector;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public Angle getAngle() {
        return angle;
    }

    public void setAngle(Angle angle) {
        this.angle = angle;
    }

    public Point[] getPoints() {
        return points;
    }

    public void setPoints(Point[] points) {
        this.points = points;
    }
}