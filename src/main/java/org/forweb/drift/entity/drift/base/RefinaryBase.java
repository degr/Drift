package org.forweb.drift.entity.drift.base;

import org.forweb.drift.entity.drift.Types;
import org.forweb.drift.utils.MassUtils;
import org.forweb.geometry.shapes.Point;


public class RefinaryBase extends AbstractBase {

    public static Point[] generatePoints() {
        Point[] points = new Point[9];
        int k = 20;
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
    }

    @Override
    public String getType() {
        return Types.refinary.toString();
    }
}
