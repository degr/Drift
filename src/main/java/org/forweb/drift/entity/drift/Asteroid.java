package org.forweb.drift.entity.drift;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.forweb.drift.utils.IncrementalId;
import org.forweb.drift.utils.MassUtils;
import org.forweb.geometry.misc.Vector;
import org.forweb.geometry.shapes.Point;
import org.jbox2d.common.Vec2;

import java.util.Random;

public class Asteroid extends BaseObject {

    private static Vec2[] createPoints() {
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

    private double rotationSpeed;
    private boolean alive;

    /**
     * Used from asteroid factory
     * @param x position
     * @param y position
     * @param id of object
     */
    public Asteroid(double x, double y, int id) {
        this(x, y, Asteroid.createPoints(), id, 0);
    }

    /**
     * used from Asteriod::onImpact method
     * @param x
     * @param y
     * @param points
     * @param id
     */
    public Asteroid(double x, double y, Vec2[] points, int id) {
        this(x, y, points, id, 0);
    }

    protected Asteroid(double x, double y, Vec2[] points, int id, double angle) {
        super(x, y, angle, null, id);
        Random random = new Random();
        setVector(new Vector(random.nextDouble() * 2 - 1, random.nextDouble() * 2 - 1));

        if(points.length < 3) {
            System.out.println("less 3");
        }
        this.setPoints(points);
        this.rotationSpeed = Math.random() * 0.2 - 0.1;
        this.alive = true;
    }

    public boolean isAlive() {
        return this.alive;
    }

    public BaseObject[] update() {
        Vector v = getVector();
        setX(getX() + v.x);
        setY(getY() + v.y);
        getAngle().append(this.rotationSpeed);
        return null;
    }

    @Override
    public String getType() {
        return Types.asteroid.toString();
    }

    @JsonIgnore
    @Override
    public boolean isRelativePoints() {
        return true;
    }

    public boolean hasImpact(BaseObject baseObject) {
        if (baseObject instanceof Asteroid || baseObject instanceof Explosion) {
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
        Vec2[] points = getPoints();
        if (points.length > 3) {
            out = createNewAsteroids(x, y, ids);
        } else {
            out = new BaseObject[1];
        }
        out[out.length - 1] = new Explosion(x, y, getVector(), 30, 100, ids.get());
        return out;
    }

    private BaseObject[] createNewAsteroids(double x, double y, IncrementalId ids) {
        int firstSize = 3;
        int start = 0;
        double angle = this.getAngle().doubleValue();
        double totalSquare = MassUtils.getSquare(this.getPoints());

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
                return processByTwoPoints(p1, p2, x, y, angle, ids);
            } else {
                start++;
            }
        }
        //return new BaseObject[1];
    }

    private static BaseObject[] processByTwoPoints(Vec2[] p1, Vec2[] p2, double x, double y, double angle, IncrementalId ids) {
        BaseObject[] out = new BaseObject[3];
        double[] aCenter = MassUtils.getCenterOfMass(p1[0], p1[1], p1[2]);
        p1 = MassUtils.setToCenter(p1, aCenter);
        out[0] = new Asteroid(x + aCenter[0], y + aCenter[1], p1, ids.get(), angle);

        aCenter = MassUtils.getCenterOfMass(p2);
        p2 = MassUtils.setToCenter(p2, aCenter);
        out[1] = new Asteroid(x + aCenter[0], y + aCenter[1], p2, ids.get(), angle);
        return out;
    }

    public double getRotationSpeed() {
        return rotationSpeed;
    }
}
