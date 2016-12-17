package org.forweb.drift.entity.drift;

import org.forweb.geometry.misc.Angle;
import org.forweb.geometry.misc.Vector;
import org.forweb.geometry.shapes.Point;

public class SpaceShip extends RelativePointsObject{

    static Point[] points = new Point[]{new Point(-12, 12), new Point(15, 0), new Point(-12, -12)};
    private boolean turnToLeft;
    private boolean turnToRight;
    private boolean hasAcceleration;
    private Gun[] guns;
    private boolean fireStarted;
    private boolean isAlive;
    private Room room;

    public SpaceShip(double x, double y, Room room) {
        super(x, y, 0, SpaceShip.points);
        this.setVector(new Vector(0, 0));
        this.room = room;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public void update() {
        Angle angle = getAngle();
        Vector vector = getVector();
        if(this.hasAcceleration){
            double acceleration = 0.1;
            double x = angle.cos() * acceleration;
            double y = angle.sin() * acceleration;
            this.getVector().append(new Vector(x, y));
        }
        if(this.turnToLeft){
            angle.append(-0.07);
        }
        if(this.turnToRight){
            angle.append(0.07);
        }

        setX(getX() + vector.x);
        setY(getY() + vector.y);
        if(this.fireStarted) {
            for(int i = 0; i < this.guns.length; i++) {
                Gun gun = this.guns[i];
                if(gun.canFire()) {
                    Bullet bullet = gun.fire();
                    bullet.correct(this);
                    Angle bulletAngle = angle.sum(Math.PI);
                    vector.append(new Vector(bulletAngle.cos() * 0.2, bulletAngle.sin() * 0.2));
                    room.addObject(bullet);
                }
            }
        }
    }



    public BaseObject[] onImpact(BaseObject object) {
        if(object instanceof Bullet) {
            if(((Bullet)object).getSpaceShip() == this) {
                return null;
            }
        } else if(object instanceof Gun) {
            return null;
        }
        this.isAlive = false;
        BaseObject[] out = new BaseObject[1];
        out[0] = new Explosion(getX(), getY(), getVector(), 30);
        return out;
    };




    public boolean isTurnToLeft() {
        return turnToLeft;
    }

    public void setTurnToLeft(boolean turnToLeft) {
        this.turnToLeft = turnToLeft;
    }

    public boolean isTurnToRight() {
        return turnToRight;
    }

    public void setTurnToRight(boolean turnToRight) {
        this.turnToRight = turnToRight;
    }

    public boolean isHasAcceleration() {
        return hasAcceleration;
    }

    public void setHasAcceleration(boolean hasAcceleration) {
        this.hasAcceleration = hasAcceleration;
    }

    public Gun[] getGuns() {
        return guns;
    }

    public void setGuns(Gun[] guns) {
        this.guns = guns;
    }

    public boolean isFireStarted() {
        return fireStarted;
    }

    public void setFireStarted(boolean fireStarted) {
        this.fireStarted = fireStarted;
    }

    public void setAlive(boolean alive) {
        isAlive = alive;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }
}