package org.forweb.drift.utils;

import org.forweb.geometry.shapes.Point;

public class MassUtils {

    public static double[] getCenterOfMass(Point p, Point q, Point r) {
        double[] out = new double[2];
        out[0] = (p.getX() + q.getX() + r.getX()) / 3;
        out[1] = (p.getY() + q.getY() + r.getY()) / 3;
        return out;
    }

    /**
     * Return center points center of mass. All points should surround 0/0.
     * @param points for calculation
     * @return double[0] = x, double[1] = y
     */
    public static double[] getCenterOfMass(Point[] points) {
        int l = points.length;
        double x = 0;
        double y = 0;
        double totalMass = 0;
        Point shift = new Point(0, 0);
        while(l-- > 0) {
            Point p2 = points[l];
            Point p3 = l == 0 ? points[points.length - 1] : points[l - 1];
            double[] center = getCenterOfMass(shift, p2, p3);
            //square = x1y2 + x2y3 + x3y1 – x1y3 – x2y1 – x3y2.
            double mass = getSquare(0, 0, p2.getX(), p2.getY(), p3.getX(), p3.getY());
            totalMass += mass;
            x += (center[0]) * mass;
            y += (center[1]) * mass;
        }

        double[] out = new double[2];
        out[0] = x / totalMass;
        out[1] = y / totalMass;
        return out;
    }


    public static double getSquare(Point[] p) {
        double area = 0;
        int N = p.length;
        for(int i = 1; i+1<N; i++){
            area += _getSquare(p[0].getX(), p[0].getY(), p[i].getX(), p[i].getY(), p[i + 1].getX(), p[i + 1].getY());
        }
        return Math.abs(area/2.0);
    }

    public static double getSquare(double x1, double y1, double x2, double y2, double x3, double y3) {
        return Math.abs(x1 * y2 + x2 * y3 + x3 * y1 - x1 * y3 - x2 * y1 - x3 * y2);
    }
    private static double _getSquare(double x1, double y1, double x2, double y2, double x3, double y3) {
        return x1 * y2 + x2 * y3 + x3 * y1 - x1 * y3 - x2 * y1 - x3 * y2;
    }


    public static Point[] setToCenter(Point[] points) {
        double[] centerOfMass = MassUtils.getCenterOfMass(points);
        return setToCenter(points, centerOfMass);
    }
    public static Point[] setToCenter(Point[] points, double[] centerOfMass) {
        for (int i = 0; i < points.length; i++) {
            Point p = points[i];
            points[i] = new Point(p.getX() - centerOfMass[0], p.getY() - centerOfMass[1]);
        }
        return points;
    }
}
