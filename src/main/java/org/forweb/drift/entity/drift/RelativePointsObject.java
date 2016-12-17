package org.forweb.drift.entity.drift;


import org.forweb.geometry.services.PointService;
import org.forweb.geometry.shapes.Point;

public class RelativePointsObject extends BaseObject{

    public static Point zero = new Point(0, 0);


    RelativePointsObject(double x, double y, double angle, Point[] points) {
        super(x, y, angle, points);
    }

    public Point[] getPoints() {
        Point[] points = super.getPoints();
        Point[] out = new Point[points.length];
        for(int i = 0; i < points.length; i++) {
            Point p = PointService.translate(zero, points[i], getAngle());
            out[i] = new Point(p.getX() + getX(), p.getY() + getY());
        }
        return out;
    }
}
