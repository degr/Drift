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
        int l = points.length - 1;
        double x = 0;
        double y = 0;
        double shiftX = shift.getX();
        double shiftY = shift.getY();
        while(l-- > 1) {
            double[] center = getCenterOfMass(points[l], points[l + 1], points[0]);
            x += center[0] + shiftX;
            y += center[1] + shiftY;
        }

        double[] out = new double[2];
        out[0] = x / (points.length - 2);
        out[1] = y / (points.length - 2);
        return out;
    }

}
