package org.forweb.drift.entity.drift.controlable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.forweb.drift.entity.drift.*;
import org.forweb.drift.entity.drift.controlable.base.AbstractBase;
import org.forweb.drift.entity.drift.controlable.base.RefinaryBase;
import org.forweb.drift.utils.IncrementalId;
import org.forweb.geometry.misc.Angle;
import org.forweb.geometry.misc.Vector;
import org.forweb.geometry.shapes.Point;

public class SpaceShip extends Controlable {

    @JsonIgnore
    private static Point[] spaceShipPoints = new Point[]{new Point(-12, 12), new Point(15, 0), new Point(-12, -12)};

    @JsonIgnore
    private IncrementalId ids;
    private AbstractBase base;


    public SpaceShip(double x, double y, IncrementalId ids) {
        super(x, y, 0, SpaceShip.spaceShipPoints, ids.get());
        this.setVector(new Vector(0, 0));
        this.ids = ids;

        this.addGun(new Gun(0, 0, ids.get()));
        setInvincible(true);
    }


    public BaseObject[] update() {
        super.update();
        BaseObject[] bullet = null;
        if(this.isFireStarted()) {
            Angle angle = getAngle();
            Vector vector = getVector();
            Gun[] guns = getGuns();
            for(int i = 0; i < guns.length; i++) {
                Gun gun = guns[i];
                if(gun.canFire()) {
                    bullet = new BaseObject[1];
                    Bullet b = gun.fire(ids);
                    b.correct(this);
                    bullet[0] = b;

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
        return Types.ship.toString();
    }


    @JsonIgnore
    public BaseObject[] onImpact(BaseObject object, IncrementalId ids) {
        this.setAlive(false);
        BaseObject[] out = new BaseObject[1];
        out[0] = new Explosion(getX(), getY(), getVector(), 30, ids.get());
        return out;
    };





    @Override
    public boolean hasImpact(BaseObject baseObject) {
        if(baseObject instanceof Explosion ||
                baseObject instanceof Gun ||
                (baseObject instanceof Bullet && ((Bullet) baseObject).getShip() == this.getId())
        ) {
            return false;
        } else if(base != null && baseObject == base) {
            return base.hasImpact(this);
        } else {
            return super.hasImpact(baseObject);
        }
    }

    public void setOnDock(AbstractBase base) {
        this.base = base;
    }
}