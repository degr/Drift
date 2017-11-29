package org.forweb.drift.entity.drift.base;

import org.forweb.drift.entity.drift.BaseObject;
import org.forweb.drift.entity.drift.SpaceShip;
import org.forweb.drift.entity.drift.Types;
import org.forweb.drift.utils.MassUtils;
import org.forweb.geometry.services.LineService;
import org.forweb.geometry.services.PointService;
import org.forweb.geometry.shapes.Point;


public class RefinaryBase extends AbstractBase {

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
        super(x, y, angle, RefinaryBase.generatePoints(), id);
        landingPoint = new Point(this.points[2].getX(), this.points[2].getY() + 5 * k);
    }

    @Override
    public String getType() {
        return Types.refinary.toString();
    }

    @Override
    public boolean hasImpact(BaseObject baseObject) {
        boolean out = super.hasImpact(baseObject);
        if(baseObject instanceof SpaceShip) {
            System.out.println();
        }
        if(!out && baseObject instanceof SpaceShip) {
            SpaceShip spaceShip = (SpaceShip)baseObject;
            Point p = PointService.translate(
                    new Point(0, 0),
                    landingPoint,
                    this.getAngle()
            );
            double distance = LineService.getDistance(
                    new Point(spaceShip.getX(), spaceShip.getY()),
                    new Point(p.getX() + this.getX(), p.getY() + this.getY())
            );
            if(distance < 80) {
                System.out.println("landing started");
            }
        }
        return out;
    }
}
