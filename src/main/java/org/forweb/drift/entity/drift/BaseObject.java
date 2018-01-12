package org.forweb.drift.entity.drift;


import com.fasterxml.jackson.annotation.JsonIgnore;
import org.forweb.drift.utils.IncrementalId;
import org.forweb.drift.utils.PolygonalUtils;
import org.forweb.geometry.misc.Angle;
import org.forweb.geometry.misc.Vector;
import org.forweb.geometry.services.LineService;
import org.forweb.geometry.services.PointService;
import org.forweb.geometry.shapes.Line;
import org.forweb.geometry.shapes.Point;
import org.jbox2d.common.Vec2;

abstract public class BaseObject {
    private double x;
    private double y;
    private Angle angle;
    protected Vec2[] points;
    private Vector vector;
    private boolean invincible;


    private int id;
    public BaseObject(double x, double y, double angle, int id) {
        this(x, y, angle, null, id);
    }


    public BaseObject(double x, double y, double angle, Vec2[] points, int id) {
        this.id = id;
        this.setX(x);
        this.setY(y);
        this.setAngle(new Angle(angle));
        this.setPoints(points);
        this.invincible = false;
    }

    abstract public String getType();
    @JsonIgnore
    abstract public boolean isRelativePoints();

    private static Vec2 zero = new Vec2(0, 0);

    private static Vec2[] translatePoints(Vec2[] points, double x, double y, Angle angle) {
        Vec2[] out = new Vec2[points.length];
        for(int i = 0; i < points.length; i++) {
            Vec2 p = PolygonalUtils.translate(zero, points[i], angle);
            out[i] = new Vec2(p.x + x, p.y + y);
        }
        return out;
    }

    private static Vec2[] appendLastPoint(Vec2[] input) {
        if(input.length == 2) {
            return input;
        } else {
            Vec2[] out = new Vec2[input.length + 1];
            System.arraycopy(input, 0, out, 0, input.length);
            out[input.length] = input[0];
            return out;
        }
    }

    public int getId() {
        return id;
    }

    public boolean hasImpact(BaseObject baseObject) {
        if (!baseObject.isAlive() || !this.isAlive() || this.isInvincible() || baseObject.isInvincible()) {
            return false;
        }
        Vec2[] thisPoints = getPoints();
        if (thisPoints == null) {
            return false;
        }
        if (this.isRelativePoints()) {
            thisPoints = BaseObject.translatePoints(getPoints(), getX(), getY(), getAngle());
        }
        int thisLength = thisPoints.length;
        if (thisLength <= 1) {
            return false;
        }
        thisPoints = appendLastPoint(thisPoints);
        thisLength = thisPoints.length;
        try {
            /*if((this instanceof Bullet && baseObject instanceof Asteroid) || (baseObject instanceof Bullet && this instanceof Asteroid)) {
                double distance = LineService.getDistance(this.getX(), this.getY(), baseObject.getX(), baseObject.getY());
                if(distance < 100) {
                    System.out.println(distance);
                }
            }*/

            Vec2[] thatPoints = baseObject.getPoints();
            if (thatPoints == null) {
                return false;
            }
            if (baseObject.isRelativePoints()) {
                thatPoints = BaseObject.translatePoints(
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
            thatPoints = appendLastPoint(thatPoints);


            while (--thisLength > 0) {
                Point p = new Point(thisPoints[thisLength].x, thisPoints[thisLength].y);
                Point p1 = new Point(thisPoints[thisLength - 1].x, thisPoints[thisLength - 1].y);
                Line line1 = new Line(p, p1);
                thatLength = thatPoints.length;
                while (--thatLength > 0) {
                    Point p2 = new Point(thatPoints[thatLength].x, thatPoints[thatLength].y);
                    Point p3 = new Point(thatPoints[thatLength - 1].x, thatPoints[thatLength - 1].y);
                    Line line2 = new Line(p2, p3);
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

    public BaseObject[] onImpact(BaseObject object, IncrementalId ids){
        return null;
    }

    public BaseObject[] update() {
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

    public Vec2[] getPoints() {
        return points;
    }

    public void setPoints(Vec2[] points) {
        this.points = points;
    }

    public int hashCode() {
        return id;
    }

    public boolean isInvincible() {
        return invincible;
    }

    public void setInvincible(boolean invincible) {
        this.invincible = invincible;
    }
}