package org.forweb.drift.utils;

import org.forweb.geometry.shapes.Point;
import org.jbox2d.common.Vec2;

import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

public class MassUtils {

    public static double[] getCenterOfMass(Vec2 p, Vec2 q, Vec2 r) {
        double[] out = new double[2];
        out[0] = (p.x + q.x + r.x) / 3;
        out[1] = (p.y + q.y + r.y) / 3;
        return out;
    }

    /**
     * Return center points center of mass. All points should surround 0/0.
     * @param points for calculation
     * @return double[0] = x, double[1] = y
     */
    public static double[] getCenterOfMass(Vec2[] points) {
        if(points.length == 1) {
            return new double[]{points[0].x, points[0].y};
        } else if(points.length == 2) {
            return new double[]{(points[0].x + points[1].x) / 2, (points[0].y + points[1].y) / 2};
        } else {

            double sum = 0.0D;
            double[] out = new double[2];
            out[0] = 0;
            out[1] = 0;

            for (int i = 0; i<points.length; i++){
                Vec2 v1 = points[i];
                Vec2 v2 = points[(i + 1) % points.length];
                double cross = v1.x*v2.y - v1.y*v2.x;
                sum += cross;
                out[0] = ((v1.x + v2.x) * cross) + out[0];
                out[1] = ((v1.y + v2.y) * cross) + out[1];
            }
            if(sum == 0) {
                return getCenterOfMass(removeDuplicatedPoints(points));
            } else {
                double z = 1.0D / (3.0D * sum);
                out[0] = out[0] * z;
                out[1] = out[1] * z;
                return out;
            }
            /*
            int l = points.length;
            double x = 0;
            double y = 0;
            double totalMass = 0;
            Point shift = new Point(0, 0);
            while (l-- > 0) {
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
            return out;*/
        }
    }

    private static Vec2[] removeDuplicatedPoints(Vec2[] points) {
        Vec2[] container = new Vec2[points.length];
        int max = 0;
        for (Vec2 point : points) {
            for(int i = 0; i < container.length; i++) {
                if(container[i] == null) {
                    max++;
                    container[i] = point;
                    break;
                } else {
                    if(container[i].x == point.x && container[i].y == point.y) {
                        break;
                    }
                }
            }
        }
        Vec2[] out = new Vec2[max];
        for(;max > 0; max--) {
            out[max - 1] = container[max - 1];
        }
        return out;
    }


    public static double getSquare(Vec2[] p) {
        double area = 0;
        int N = p.length;
        for(int i = 1; i+1<N; i++){
            area += _getSquare(p[0].x, p[0].y, p[i].x, p[i].y, p[i + 1].x, p[i + 1].y);
        }
        return Math.abs(area/2.0);
    }

    public static double getSquare(double x1, double y1, double x2, double y2, double x3, double y3) {
        return Math.abs(x1 * y2 + x2 * y3 + x3 * y1 - x1 * y3 - x2 * y1 - x3 * y2);
    }
    private static double _getSquare(double x1, double y1, double x2, double y2, double x3, double y3) {
        return x1 * y2 + x2 * y3 + x3 * y1 - x1 * y3 - x2 * y1 - x3 * y2;
    }


    public static Vec2[] setToCenter(Vec2[] points) {
        double[] centerOfMass = MassUtils.getCenterOfMass(points);
        return setToCenter(points, centerOfMass);
    }
    public static Vec2[] setToCenter(Vec2[] points, double[] centerOfMass) {
        for (int i = 0; i < points.length; i++) {
            Vec2 p = points[i];
            points[i] = new Vec2(p.x - centerOfMass[0], p.y - centerOfMass[1]);
        }
        return points;
    }
}
