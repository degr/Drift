package org.forweb.drift.entity.drift.spaceships;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.forweb.drift.entity.drift.BaseObject;
import org.forweb.drift.entity.drift.Gun;
import org.forweb.drift.utils.ArrayUtils;
import org.forweb.geometry.misc.Angle;
import org.forweb.geometry.misc.Vector;
import org.forweb.geometry.shapes.Point;

public abstract class Controlable extends BaseObject{

    private int turn;
    private boolean hasAcceleration;
    private boolean isAlive;
    private Gun[] guns;
    private boolean fireStarted;
    @JsonIgnore
    private boolean updateAcceleration;
    @JsonIgnore
    private boolean updateTurn;
    @JsonIgnore
    private boolean updateFire;
    @JsonIgnore
    private boolean updateInvincible;
    @JsonIgnore
    private boolean updateRequire;

    public Controlable(double x, double y, double angle, Point[] points, int id) {
        super(x, y, angle, points, id);

        isAlive = true;
        turn = 0;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public BaseObject[] update() {
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
        return null;
    }

    @JsonIgnore
    @Override
    public boolean isRelativePoints() {
        return true;
    }

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

    @JsonIgnore
    public boolean isUpdateRequire() {
        return updateRequire;
    }

    public void addGun(Gun gun) {
        int gunPosition = -1;
        if(this.guns == null) {
            this.guns = new Gun[1];
            gunPosition = 0;
        } else {
            for (int i = 0; i < guns.length; i++) {
                if(guns[i] == null) {
                    gunPosition = i;
                    break;
                }
            }
        }
        if(gunPosition == -1) {
            this.guns = ArrayUtils.concat(this.guns, new Gun[1]);
            gunPosition = this.guns.length - 1;
        }
        this.guns[gunPosition] = gun;
    }
}
