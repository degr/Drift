package org.forweb.drift.entity.drift;


import com.fasterxml.jackson.annotation.JsonIgnore;
import org.forweb.drift.utils.IncrementalId;
import org.forweb.geometry.misc.Angle;
import org.forweb.geometry.misc.Vector;
import org.forweb.geometry.services.LineService;
import org.forweb.geometry.services.PointService;
import org.forweb.geometry.shapes.Line;
import org.forweb.geometry.shapes.Point;

abstract public class BaseObject {
    private double x;
    private double y;
    private Angle angle;
    private Point[] points;
    private Vector vector;


    private int id;
    public BaseObject(double x, double y, double angle, int id) {
        this(x, y, angle, null, id);
    }
    public BaseObject(double x, double y, double angle, Point[] points, int id) {
        this.id = id;
        this.setX(x);
        this.setY(y);
        this.setAngle(new Angle(angle));
        this.setPoints(points);
    }

    abstract public String getType();
    @JsonIgnore
    abstract public boolean isRelaivePoints();

    public static Point zero = new Point(0, 0);

    static Point[] getPoints(Point[] points, double x, double y, Angle angle) {
        Point[] out = new Point[points.length];
        for(int i = 0; i < points.length; i++) {
            Point p = PointService.translate(zero, points[i], angle);
            out[i] = new Point(p.getX() + x, p.getY() + y);
        }
        return out;
    }

    private static Point[] appendLastPoint(Point[] input) {
        Point[] out = new Point[input.length + 1];
        System.arraycopy(input, 0, out, 0, input.length);
        out[input.length] = input[0];
        return out;
    }

    public int getId() {
        return id;
    }

    public boolean hasImpact(BaseObject baseObject) {
        Point[] thisPoints = getPoints();
        if(thisPoints == null) {
            return false;
        }
        if(this.isRelaivePoints()) {
            thisPoints = BaseObject.getPoints(getPoints(), getX(), getY(), getAngle());
        }
        int thisLength = thisPoints.length;
        if(thisLength <= 1) {
            return false;
        }
        thisLength++;
        thisPoints = appendLastPoint(thisPoints);
        try {
            Point[] thatPoints = baseObject.getPoints();
            if(thatPoints == null) {
                return false;
            }
            if(baseObject.isRelaivePoints()) {
                thatPoints = BaseObject.getPoints(
                        baseObject.getPoints(),
                        baseObject.getX(),
                        baseObject.getY(),
                        baseObject.getAngle()
                );
            }
            int thatLength = thatPoints.length;
            if (thatLength <= 1) {
                return false;
            }
            thatLength++;
            thatPoints = appendLastPoint(thatPoints);

            while (--thisLength > 0) {
                Line line1 = new Line(thisPoints[thisLength], thisPoints[thisLength - 1]);
                while (--thatLength > 0) {
                    Line line2 = new Line(thatPoints[thatLength], thatPoints[thatLength - 1]);
                    if (LineService.lineHasIntersections(line1, line2)) {
                        return true;
                    }
                }
            }
        }catch (NullPointerException e){
            throw e;
        }
        return false;
    }

    public BaseObject[] onImpact(BaseObject object, IncrementalId ids){
        return null;
    }

    public void update() {

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

    public int hashCode() {
        return id;
    }
}