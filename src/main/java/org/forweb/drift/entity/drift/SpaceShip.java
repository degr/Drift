package org.forweb.drift.entity.drift;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.forweb.drift.utils.IncrementalId;
import org.forweb.geometry.misc.Angle;
import org.forweb.geometry.misc.Vector;
import org.forweb.geometry.shapes.Point;

public class SpaceShip extends BaseObject{

    @JsonIgnore
    static Point[] points = new Point[]{new Point(-12, 12), new Point(15, 0), new Point(-12, -12)};

    private int turn;
    private boolean hasAcceleration;
    private Gun[] guns;
    private boolean fireStarted;
    private boolean isAlive;

    public int updateCount = 0;

    @JsonIgnore
    private Room room;

    @JsonIgnore
    private IncrementalId ids;

    @JsonIgnore
    private boolean updateAcceleration;
    @JsonIgnore
    private boolean updateTurn;
    @JsonIgnore
    private boolean updateFire;
    @JsonIgnore
    private boolean updateInvincible;
    private boolean updateRequire;

    public SpaceShip(double x, double y, int id, Room room) {
        super(x, y, 0, SpaceShip.points, id);
        this.setVector(new Vector(0, 0));
        this.room = room;
        this.ids = room.getIds();
        setInvincible(true);
        isAlive = true;
        guns = new Gun[1];
        guns[0] = new Gun(0, 0, ids.get());
        turn = 0;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public BaseObject[] update() {
        updateCount++;
        Angle angle = getAngle();
        Vector vector = getVector();
        if(this.hasAcceleration){
            double acceleration = 0.1;
            double x = angle.cos() * acceleration;
            double y = angle.sin() * acceleration;
            this.getVector().append(new Vector(x, y));
        }
        if(this.turn == -1){
            angle.append(-0.07);
        } else if(this.turn == 1){
            angle.append(0.07);
        }

        setX(getX() + vector.x);
        setY(getY() + vector.y);
        Bullet[] bullet = null;
        if(this.fireStarted) {
            for(int i = 0; i < this.guns.length; i++) {
                Gun gun = this.guns[i];
                if(gun.canFire()) {
                    bullet = new Bullet[1];
                    bullet[0] = gun.fire(ids);
                    bullet[0].correct(this);
                    Angle bulletAngle = angle.sum(Math.PI);
                    vector.append(new Vector(bulletAngle.cos() * 0.2, bulletAngle.sin() * 0.2));
                    setUpdateFire(true);
                    setUpdateRequire(true);
                }
            }
        }
        return bullet;
    }


    @Override
    public String getType() {
        return "ship";
    }

    @JsonIgnore
    @Override
    public boolean isRelativePoints() {
        return true;
    }

    @JsonIgnore
    public BaseObject[] onImpact(BaseObject object, IncrementalId ids) {
        if(object instanceof Bullet) {
            if(((Bullet) object).getShip() == this.getId()) {
                return null;
            }
        } else if(object instanceof Gun) {
            return null;
        }
        this.setAlive(false);
        BaseObject[] out = new BaseObject[1];
        out[0] = new Explosion(getX(), getY(), getVector(), 30, ids.get());
        return out;
    };



    public boolean isHasAcceleration() {
        return hasAcceleration;
    }

    public void setHasAcceleration(boolean hasAcceleration) {
        updateAcceleration = true;
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
        updateFire = true;
        this.fireStarted = fireStarted;
    }

    public void setAlive(boolean alive) {
        isAlive = alive;
    }

    @Override
    public boolean hasImpact(BaseObject baseObject) {
        if(baseObject instanceof Explosion) {
            return false;
        } else if(baseObject instanceof Bullet && ((Bullet) baseObject).getShip() == this.getId()) {
            return false;
        } else {
            return super.hasImpact(baseObject);
        }
    }

    @JsonIgnore
    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public void setTurn(int turn) {
        updateTurn = true;
        this.turn = turn;
    }
    @JsonIgnore
    public boolean isUpdateAcceleration() {
        return updateAcceleration;
    }
    public void setUpdateAcceleration(boolean updateAcceleration) {
        this.updateAcceleration = updateAcceleration;
    }
    @JsonIgnore
    public boolean isUpdateTurn() {
        return updateTurn;
    }
    public void setUpdateTurn(boolean updateTurn) {
        this.updateTurn = updateTurn;
    }
    @JsonIgnore
    public boolean isUpdateFire() {
        return updateFire;
    }
    public void setUpdateFire(boolean updateFire) {
        this.updateFire = updateFire;
    }

    public int getTurn() {
        return turn;
    }

    @JsonIgnore
    public void setUpdateInvincible(boolean updateInvincible) {
        this.updateInvincible = updateInvincible;
    }

    @JsonIgnore
    public boolean isUpdateInvincible() {
        return updateInvincible;
    }

    public void setUpdateRequire(boolean updateRequire) {
        this.updateRequire = updateRequire;
    }

    public boolean isUpdateRequire() {
        return updateRequire;
    }
}