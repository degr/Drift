package org.forweb.drift.utils;

import org.forweb.drift.entity.drift.PolygonalObject;
import org.forweb.geometry.misc.Angle;
import org.forweb.geometry.misc.Vector;
import org.forweb.geometry.services.LineService;
import org.forweb.geometry.services.PointService;
import org.forweb.geometry.shapes.Circle;
import org.forweb.geometry.shapes.Line;
import org.forweb.geometry.shapes.Point;

public class PolygonalUtils {

    private static Point zero = new Point(0, 0);
    private static final double reverse = 0.01;

    public static Point[] translatePoints(Point[] points, double x, double y, Angle angle, boolean appendLastPoint) {
        int length = points.length + (points.length > 2 && appendLastPoint ? 1 : 0);
        Point[] out = new Point[length];
        for (int i = 0; i < points.length; i++) {
            Point p = PointService.translate(zero, points[i], angle);
            out[i] = new Point(p.getX() + x, p.getY() + y);
        }
        if (appendLastPoint && points.length > 2) {
            out[points.length] = out[0];
        }
        return out;
    }

    public static void applyForceToPoint(PolygonalObject object, Vector force, Point point) {
        double x = object.getX();
        double y = object.getY();
        double radius = object.getRadius();
        if (PointService.pointBelongToCircle(point, new Circle(x, y, radius)) >= 0) {
            double rotationScale = calculateRotationScale(object, force, point);
            applyForceToCenter(object, calculateVector(object, force, rotationScale));
            object.getRotation().append(calculateAngle(object, force, point, rotationScale));
        }
    }

    public static double calculateAngle(PolygonalObject object, Vector force, Point point, double rotationScale) {
        double angle = (Math.PI * rotationScale / object.getMass());
        Point pointOfApplication = new Point(point.getX() - object.getX(), point.getY() - object.getY());
        double torque = pointOfApplication.getX() * force.y - pointOfApplication.getY() * force.x;
        if (torque < 0) {
            angle = -angle;
        }
        return angle;
    }


    public static void applyForceToCenter(PolygonalObject object, Vector force) {
        double mass = object.getMass();
        object.getVector().append(new Vector(force.x / mass, force.y / mass));
    }

    public static Point[] translatePoints(Point[] points, double x, double y, Angle angle) {
        Point[] out = new Point[points.length];
        for (int i = 0; i < points.length; i++) {
            Point p = PointService.translate(zero, points[i], angle);
            out[i] = new Point(p.getX() + x, p.getY() + y);
        }
        return out;
    }


    public static Vector calculateVector(PolygonalObject object, Vector force, double rotationScale) {
        //double rotationScale = calculateRotationScale(object, force, point);
        double movementScale = 1 - rotationScale;
        return new Vector(force.x * movementScale, force.y * movementScale);
    }

    public static double calculateRotationScale(PolygonalObject object, Vector force, Point point) {
        double a = force.y,
                b = -force.x,
                c = force.x * point.getY() - force.y * point.getX();
        double bottom = a * a + b * b;
        if (bottom == 0) {
            return 1;
        } else {
            double distance = Math.abs(a * object.getX() + b * object.getY() + c) / Math.sqrt(bottom);
            //let's think that if power applied to max radius, half will be used for rotation, half for movement
            return distance / object.getRadius();
        }
    }

    public static Point hasImpact(PolygonalObject baseObject, PolygonalObject object) {

        if (!baseObject.isAlive() || !object.isAlive() || object.isInvincible() || baseObject.isInvincible()) {
            return null;
        }
        Point[] thisPoints = object.getPoints();
        if (thisPoints == null) {
            return null;
        }
        int thisLength = thisPoints.length;
        if (thisLength <= 1) {
            return null;
        }
        thisPoints = appendLastPoint(thisPoints);
        thisLength = thisPoints.length;
        try {
            Point[] thatPoints = baseObject.getPoints();
            if (thatPoints == null) {
                return null;
            }
            int thatLength = thatPoints.length;
            if (thatLength <= 1) {
                return null;
            }
            thatPoints = appendLastPoint(thatPoints);
            Point[] out = null;
            while (--thisLength > 0) {
                Line line1 = new Line(thisPoints[thisLength], thisPoints[thisLength - 1]);
                thatLength = thatPoints.length;
                while (--thatLength > 0) {
                    Line line2 = new Line(thatPoints[thatLength], thatPoints[thatLength - 1]);
                    Point[] p = LineService.lineLineIntersections(line1, line2);
                    if (p != PointService.EMPTY) {
                        if (out == null) {
                            out = p;
                        } else {
                            out = ArrayUtils.concat(out, p);
                        }
                    }
                }
            }
            if (out != null) {
                double[] center = MassUtils.getCenterOfMass(out);
                return new Point(center[0], center[1]);
            }
        } catch (NullPointerException e) {
            throw e;
        }
        return null;
    }

    private static Point[] appendLastPoint(Point[] input) {
        if (input.length == 2) {
            return input;
        } else {
            Point[] out = new Point[input.length + 1];
            System.arraycopy(input, 0, out, 0, input.length);
            out[input.length] = input[0];
            return out;
        }
    }

    public static void resolveCollision(PolygonalObject obj1, PolygonalObject obj2, Point impact) {
        double mass1 = obj1.getMass();
        double mass2 = obj2.getMass();
        Vector vector1 = obj1.getVector();
        Vector vector2 = obj2.getVector();



        Vector obj1Vector = new Vector(vector1.x * mass1, vector1.y * mass1);
        Vector obj2Vector = new Vector(vector2.x * mass2, vector2.y * mass2);

        Vector sum = obj1Vector.plus(obj2Vector);
        double massScale1 = mass1 / (mass1 + mass2);
        double massScale2 = 1 - massScale1;

        double forceReduction = 9d/10d;

        double angle = Math.abs(getVectorAngle(obj1Vector) - getVectorAngle(obj2Vector));
        Vector result1;
        Vector result2;

        double length1 = length(vector1);
        double length2 = length(vector2);
        double result1Length;
        double result2Length;
        result1 = new Vector(sum.x * massScale1, sum.y * massScale1);
        result2 = new Vector(sum.x * massScale2, sum.y * massScale2);
        if(angle < Math.PI) {
            //result vector strong
            if(length1 > length2) {
                double deltaX1, deltaY1, deltaX2, deltaY2;
                //first object fly faster than second, after collision first object should fly slowly than second
                if(mass1 > mass2) {
                    //first object more heavy than second, it should downscale his vector little bit
                    //and upscale second object vector to appropriate value
                    deltaX1 = result1.x * 0.1D;
                    deltaY1 = result1.y * 0.1D;
                    deltaX2 = deltaX1 * massScale2 / massScale1;
                    deltaY2 = deltaY1 * massScale2 / massScale1;
                } else {
                    //second object more heavy, or have same mass.
                    //second object should upscale it speed
                    //first object
                    deltaX2 = result2.x * 0.1D;
                    deltaY2 = result2.y * 0.1D;
                    deltaX1 = deltaX2 * massScale1 / massScale2;
                    deltaY1 = deltaY2 * massScale1 / massScale2;
                }
                result1 = new Vector(result1.x - deltaX1, result1.y - deltaY1);
                result2 = new Vector(result2.x + deltaX2, result2.y + deltaY2);
            }
        } else {
            //result vector week

        }
        obj1.setVector(new Vector(0, 0));
        obj2.setVector(new Vector(0, 0));
        obj1.applyForceToPoint(result1, impact);
        obj2.applyForceToPoint(result2, impact);

        obj1.setAngle(new Angle(0));
        obj2.setAngle(new Angle(0));
        obj1.setRotation(new Angle(0));
        obj2.setRotation(new Angle(0));




        if(true) {
            return;
        }

        double force1 = obj1.getMass() * length(obj1.getVector());
        double force2 = obj2.getMass() * length(obj2.getVector());
        double force = force1 + force2;
        double scale1 = force1 / (force1 + force2);
        double scale2 = 1 - scale1;

        Vector reverted1 = revertByValue(obj1.getVector(), force * scale1);
        Vector reverted2 = revertByValue(obj2.getVector(), force * scale2);


        Vector forceOnImpact1 = new Vector(obj1.getVector().x * obj1.getMass(), obj1.getVector().y * obj1.getMass());
        Vector forceOnImpact2 = new Vector(obj2.getVector().x * obj2.getMass(), obj2.getVector().y * obj2.getMass());

        obj1.applyForceToPoint(reverted1, impact);
        obj2.applyForceToPoint(reverted2, impact);

        double torque1 = multiple(
                new Vector(impact.getX() - obj1.getX(), impact.getY() - obj1.getY()),
                forceOnImpact1
        );
        double torque2 = multiple(
                new Vector(impact.getX() - obj2.getX(), impact.getY() - obj2.getY()),
                forceOnImpact2
        );

    }

    private static double getVectorAngle(Vector vector) {
        if(vector.y != 0) {
            return Math.atan(vector.x / vector.y);
        } else {
            return vector.x >= 0 ? 0 : Math.PI;
        }
    }

    private static Vector revertByValue(Vector vector, double value) {
        double length = length(vector);
        if(length == 0) {
            return new Vector(0, 0);
        } else {
            return new Vector(vector.x * value / length, vector.y * value / length);
        }
    }

    private static double length(Vector vector) {
        return Math.sqrt(vector.x * vector.x + vector.y * vector.y);
    }

    private static double multiple(Vector v1, Vector v2) {
        return v1.x * v2.y - v1.y * v2.x;
    }

    private static double vectorLength(Vector v) {
        return Math.sqrt(v.x * v.x + v.y * v.y);
    }

    protected static double reverseRotation(Angle rotation) {
        double value = rotation.doubleValue();
        if (value > Math.PI) {
            //Math.PI * 2 - (Math.PI * 2 - value) * 0.9;
            return (Math.PI * 2 - value) * reverse;
        } else if (value > 0) {
            return (Math.PI * 2) - value * reverse;
        } else {
            return 0;
        }
    }

    class Vector3 {
        public double x, y, z;

        public Vector3() {
            this(0, 0, 0);
        }

        public Vector3(double x, double y, double z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }
    }
}
