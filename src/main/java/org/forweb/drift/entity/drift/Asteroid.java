package org.forweb.drift.entity.drift;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.forweb.drift.utils.IncrementalId;
import org.forweb.drift.utils.MassUtils;
import org.forweb.geometry.misc.Vector;
import org.forweb.geometry.shapes.Point;

import java.util.Random;

public class Asteroid extends BaseObject{


    private double rotationSpeed;
    private boolean alive;

    public Asteroid(double x, double y, Point[] points, int id) {
        super(x, y, 0, null, id);
        Random random = new Random();
        setVector(new Vector(random.nextDouble() * 2 - 1, random.nextDouble() * 2 - 1));
        if(points == null || points.length == 0){
            int limit = (int)Math.ceil(random.nextDouble() * 7) + 3;
            double sector = Math.PI * 2 / limit;
            points = new Point[limit];
            double angle;
            while (limit-- > 0) {
                angle = (Math.random() * (sector)) + sector * limit;
                double distance = Math.random() * 90;
                points[limit] = new Point(
                        Math.cos(angle) * distance,
                        Math.sin(angle) * distance
                );
            }
        }
        setPoints(points);
        double[] centerOfMass = MassUtils.getCenterOfMass(points, new Point(x, y));
        setX(centerOfMass[0]);
        setY(centerOfMass[1]);
        this.rotationSpeed = Math.random() * 0.2 - 0.1;
        this.alive = true;
    }

    public boolean isAlive(){
        return this.alive;
    }

    public BaseObject[] update(){
        Vector v = getVector();
        setX(getX() + v.x);
        setY(getY() + v.y);
        getAngle().append(this.rotationSpeed);
        return null;
    }

    @Override
    public String getType() {
        return "asteroid";
    }

    @JsonIgnore
    @Override
    public boolean isRelativePoints() {
        return true;
    }

    public boolean hasImpact(BaseObject baseObject) {
        if(baseObject instanceof Asteroid || baseObject instanceof Explosion) {
            return false;
        } else {
            return super.hasImpact(baseObject);
        }
    }

    public BaseObject[] onImpact(BaseObject object, IncrementalId ids) {
        this.alive = false;
        BaseObject[] out;
        double x = this.getX();
        double y = this.getY();
        Point[] points = getPoints();
        if(points.length > 3) {
            out = new BaseObject[3];
            int start = (int) Math.ceil(points.length / 2);
            Point[] p1 = new Point[start + 1];
            int i;
            for(i = 0; i <= start; i++) {
                p1[i] = points[i];
            }
            out[0] = new Asteroid(x, y, p1, ids.get());
            Point[] p2 = new Point[points.length - start + 1];
            for(i = start; i < points.length; i++) {
                p2[i - start] = points[i];
            }
            p2[p2.length - 1] = points[0];
            out[1] = new Asteroid(x, y, p2, ids.get());
        } else {
            out = new BaseObject[1];
        }
        out[out.length - 1] = new Explosion(x, y, getVector(), 30, 100, ids.get());
        return out;
    }

    public double getRotationSpeed() {
        return rotationSpeed;
    }
}
