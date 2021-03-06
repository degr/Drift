package org.forweb.drift.entity.drift;


import com.fasterxml.jackson.annotation.JsonIgnore;
import org.forweb.drift.entity.drift.spaceships.SpaceShip;
import org.forweb.drift.utils.IncrementalId;
import org.forweb.geometry.misc.Angle;
import org.forweb.geometry.misc.Vector;
import org.forweb.geometry.services.PointService;
import org.forweb.geometry.shapes.Point;
import org.jbox2d.common.Vec2;

public class Bullet extends BaseObject {

    static private double getSpeed() {
        return 5;
    }

    private int ship;
    private Vec2 oldPoint;
    private double creationTime;

    public Bullet(double x, double y, double angle, int id) {
        super(x, y, angle, id);
        this.creationTime = System.currentTimeMillis() + 3000;
    }

    @Override
    public String getType() {
        return Types.bullet.toString();
    }

    @JsonIgnore
    @Override
    public boolean isRelativePoints() {
        return false;
    }

    public boolean isAlive() {
        return !(this.creationTime == 0) && !(System.currentTimeMillis() > this.creationTime);
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

        Vec2[] points = new Vec2[2];
        points[0] =new Vec2(this.getX(), this.getY());
        oldPoint = points[0];
        points[1] = new Vec2(this.getX() + 5 * angle.cos(), this.getY() + 5 * angle.sin());
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

    public BaseObject[] update() {
        this.oldPoint = this.points[0];
        Vector vector = getVector();
        this.setX(getX() + vector.x);
        this.setY(getY() + vector.y);
        this.points[0] = new Vec2(this.points[0].x + vector.x, this.points[0].y + vector.y);
        this.points[1] = new Vec2(this.points[1].x + vector.x, this.points[1].y + vector.y);
        return null;
    }

    @Override
    public Vec2[] getPoints() {
        Vec2[] out = new Vec2[2];
        out[0] = oldPoint;
        out[1] = this.points[1];
        return out;
    };

    @Override
    public BaseObject[] onImpact(BaseObject object, IncrementalId ids) {
        BaseObject[] out = new BaseObject[1];
        out[0] = new Explosion(getX(), getY(), getVector(), 8, 60, ids.get());
        this.creationTime = 0;
        return out;
    }

    public boolean hasImpact(BaseObject baseObject) {
        if(baseObject instanceof SpaceShip && baseObject.getId() == this.ship) {
            return false;
        } else {
            return super.hasImpact(baseObject);
        }
    }

    public int getShip() {
        return ship;
    }
}