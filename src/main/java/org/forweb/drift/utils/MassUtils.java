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
            Point p2 = points[l];
            Point p3 = l == 0 ? points[points.length - 1] : points[l - 1];
            double[] center = getCenterOfMass(shift, p2, p3);
            //square = x1y2 + x2y3 + x3y1 – x1y3 – x2y1 – x3y2.
            double mass = Math.abs(shiftX * p2.getY()
                    + p2.getX() * p3.getY()
                    + p3.getX() * shiftY
                    - shiftX * p3.getY()
                    - p2.getX() * shiftY
                    - p3.getX() * p2.getY()) * 1/2;
            totalMass += mass;
            x += (center[0]) * mass;
            y += (center[1]) * mass;
        }

        double[] out = new double[2];
        out[0] = x / totalMass;
        out[1] = y / totalMass;
        return out;
    }

    public static void main(String[] args) {
        Point[] p = new Point[3];
        p[0] = new Point(0,0);
        p[1] = new Point(10,0);
        p[2] = new Point(0,10);
        double[] o = getCenterOfMass(p, new Point(3, 3));
        System.out.println(o[0] + "|" + o[1]);
    }
}
