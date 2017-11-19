package org.forweb.drift.utils;

import org.forweb.geometry.shapes.Point;

public class MassUtils {

    public static double[] getCenterOfMass(Point p, Point q, Point r) {
        double[] out = new double[2];
        out[0] = (p.getX() + q.getX() + r.getX()) / 3;
        out[1] = (p.getY() + q.getY() + r.getY()) / 3;
        return out;
    }

    public static double[] getCenterOfMass(Point[] points, Point shift) {
        int l = points.length;
        double x = 0;
        double y = 0;
        double shiftX = shift.getX();
        double shiftY = shift.getY();
        double totalMass = 0;
        while(l-- > 0) {
            Point p = points[l];
            Point q = l == 0 ? points[points.length - 1] : points[l - 1];
            double[] center = getCenterOfMass(shift, p, q);
            //square = x1y2 + x2y3 + x3y1 – x1y3 – x2y1 – x3y2.
            double mass = shiftX * p.getY()
                    + p.getX() * q.getY()
                    + q.getX() * shiftY
                    - shiftX * q.getY()
                    - p.getX() * shiftY
                    - q.getX() * p.getY();
            totalMass += mass;
            x += (center[0] + shiftX) * mass;
            y += (center[1] + shiftY) * mass;
        }

        double[] out = new double[2];
        out[0] = (x / (points.length - 2)) / totalMass;
        out[1] = (y / (points.length - 2)) / totalMass;
        return out;
    }
}
