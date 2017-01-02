package org.forweb.drift.entity.drift;


import com.fasterxml.jackson.annotation.JsonIgnore;
import org.forweb.drift.utils.IncrementalId;
import org.forweb.geometry.misc.Angle;
import org.forweb.geometry.misc.Vector;
import org.forweb.geometry.services.PointService;
import org.forweb.geometry.shapes.Point;

import javax.persistence.OneToMany;
import javax.validation.OverridesAttribute;

public class Bullet extends BaseObject {

    static private double getSpeed() {
        return 5;
    }

    private int ship;
    private Point oldPoint;
    private double creationTime;

    public Bullet(double x, double y, double angle, int id) {
        super(x, y, angle, id);
        this.creationTime = System.currentTimeMillis() + 3000;
    }

    @Override
    public String getType() {
        return "bullet";
    }

    @JsonIgnore
    @Override
    public boolean isRelaivePoints() {
        return false;
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

        Point[] points = new Point[2];
        points[0] =new Point(this.getX(), this.getY());
        oldPoint = points[0];
        points[1] = new Point(this.getX() + 5 * angle.cos(), this.getY() + 5 * angle.sin());
        this.setPoints(points);

        setVector(
                new Vector(
                        Bullet.getSpeed() * angle.cos(),
                        Bullet.getSpeed() * angle.sin()
                )
        );
        Vector vector = getVector();
        vector.append(spaceShip.getVector());
        this.ship = spaceShip.getId();
    }

    public void update() {
        Point[] points = getPoints();
        this.oldPoint = points[0];
        Vector vector = getVector();
        this.setX(getX() + vector.x);
        this.setY(getY() + vector.y);
        points[0] = new Point(points[0].getX() + vector.x, points[0].getY() + vector.y);
        points[1] = new Point(points[1].getX() + vector.x, points[1].getY() + vector.y);
    }

    @Override
    public Point[] getPoints() {
        Point[] out = new Point[2];
        out[0] = oldPoint;
        out[1] = this.points[1];
        return out;
    };

    @Override
    public BaseObject[] onImpact(BaseObject object, IncrementalId ids) {
        if(object.getId() != this.ship) {
            this.creationTime = 0;
            BaseObject[] out = new BaseObject[1];
            out[0] = new Explosion(getX(), getY(), getVector(), 8, 60, ids.get());
            return out;
        }
        return null;
    }

    public int getShipId() {
        return ship;
    }
}