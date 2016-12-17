package org.forweb.drift.entity.drift;


import org.forweb.geometry.misc.Angle;
import org.forweb.geometry.misc.Vector;
import org.forweb.geometry.services.PointService;
import org.forweb.geometry.shapes.Point;

public class Bullet extends BaseObject {

    static private double getSpeed() {
        return 5;
    }

    private SpaceShip spaceShip;
    private double creationTime;

    public Bullet(double x, double y, double angle) {
        super(x, y, angle);
        this.creationTime = System.currentTimeMillis() + 3000;
    }

    public boolean isAlive() {
        return System.currentTimeMillis() - this.creationTime > 0;
    }

    public void correct(SpaceShip spaceShip) {
        setX(getX() + spaceShip.getX());
        setY(getY() + spaceShip.getY());
        Point translated = PointService.translate(
                new Point(spaceShip.getX(), spaceShip.getY()),
                new Point(getX(), getY()),
                spaceShip.getAngle()
        );
        setX(translated.getX());
        setY(translated.getY());
        Angle angle = getAngle();
        angle.append(spaceShip.getAngle());
        setVector(
                new Vector(
                        Bullet.getSpeed() * angle.cos(),
                        Bullet.getSpeed() * angle.sin()
                )
        );
        Vector vector = getVector();
        vector.append(spaceShip.getVector());
        this.spaceShip = spaceShip;
    }

    public void update() {
        double oldX = getX();
        double oldY = getY();
        Vector vector = getVector();
        this.setX(getX() + vector.x);
        this.setY(getY() + vector.y);
        Point[] points = new Point[2];
        points[0] = new Point(oldX, oldY);
        points[1] = new Point(getX(), getY());
        this.setPoints(points);
    }

    @Override
    public BaseObject[] onImpact(BaseObject object) {
        if(object != this.spaceShip) {
            this.creationTime = 0;
            BaseObject[] out = new BaseObject[1];
            out[0] = new Explosion(getX(), getY(), getVector(), 8, 60);
            return out;
        }
        return null;
    }

    public Object getSpaceShip() {
        return spaceShip;
    }
}