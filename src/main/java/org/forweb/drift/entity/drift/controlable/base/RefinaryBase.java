package org.forweb.drift.entity.drift.controlable.base;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.forweb.drift.entity.drift.BaseObject;
import org.forweb.drift.entity.drift.controlable.SpaceShip;
import org.forweb.drift.entity.drift.Types;
import org.forweb.drift.utils.MassUtils;
import org.forweb.geometry.misc.Vector;
import org.forweb.geometry.shapes.Point;


public class RefinaryBase extends AbstractBase {

    private Point detectionLandingPoint;
    private Point landingPoint;
    private static int k = 20;

    public static Point[] generatePoints() {
        Point[] points = new Point[9];
        points[0] = new Point(-2 * k, -3 * k);
        points[1] = new Point(-14 * k, -3 * k);
        points[2] = new Point(-14 * k, 2 * k);
        points[3] = new Point(-9 * k, 7 * k);
        points[4] = new Point(3 * k, 7 * k);
        points[5] = new Point(8 * k, 2 * k);
        points[6] = new Point(8 * k, -7 * k);
        points[7] = new Point(4 * k, -11 * k);
        points[8] = new Point(-2 * k, -11 * k);
        return MassUtils.setToCenter(points);
    }

    public RefinaryBase(double x, double y, double angle, int id) {
        super(x, y, id, angle, RefinaryBase.generatePoints());
        detectionLandingPoint = new Point(this.points[2].getX(), this.points[2].getY() + 5 * k);
        landingPoint = new Point(this.points[2].getX() + 5 * k, this.points[2].getY());
        this.setVector(new Vector(0, 0));
    }

    @Override
    public String getType() {
        return Types.refinary.toString();
    }

    @JsonIgnore
    @Override
    protected Point getLandingPoint() {
        return landingPoint;
    }
    @JsonIgnore
    @Override
    protected Point getDetectionLandingPoint() {
        return detectionLandingPoint;
    }

    @Override
    public boolean hasImpact(BaseObject baseObject) {
        boolean out = super.hasImpact(baseObject);
        if(baseObject instanceof SpaceShip) {
            SpaceShip spaceShip = (SpaceShip)baseObject;
            boolean land = onLand(spaceShip);
            if(land) {
                spaceShip.setOnDock(this);
                if(out) {

                }
                return false;
            } else {
                spaceShip.setOnDock(null);
            }
        }
        return out;
    }
}
