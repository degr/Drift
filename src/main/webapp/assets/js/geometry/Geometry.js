Engine.define('Geometry', ['Point', 'Line'], function () {

    var Point = Engine.require("Point");
    var Line = Engine.require("Line");

    var dPi = Math.PI * 2;

// Given three colinear points p, q, r, the function checks if
// point q lies on line segment 'pr'
    function onSegment(p, q, r)
    {
        return !!(q.x <= Math.max(p.x, r.x) && q.x >= Math.min(p.x, r.x) &&
        q.y <= Math.max(p.y, r.y) && q.y >= Math.min(p.y, r.y));
    }

// To find orientation of ordered triplet (p, q, r).
// The function returns following values
// 0 --> p, q and r are colinear
// 1 --> Clockwise
// 2 --> Counterclockwise
//return int
    function orientation(p, q, r)
    {
        // See http://www.geeksforgeeks.org/orientation-3-ordered-points/
        // for details of below formula.
        var val = (q.y - p.y) * (r.x - q.x) -
            (q.x - p.x) * (r.y - q.y);

        if (val == 0) return 0;  // colinear

        return (val > 0)? 1: 2; // clock or counterclock wise
    }

// The main function that returns true if line segment 'p1q1'
// and 'p2q2' intersect.
    function doIntersect(p1, q1, p2, q2)
    {
        // Find the four orientations needed for general and
        // special cases
        var o1 = orientation(p1, q1, p2);
        var o2 = orientation(p1, q1, q2);
        var o3 = orientation(p2, q2, p1);
        var o4 = orientation(p2, q2, q1);

        // General case
        if (o1 != o2 && o3 != o4)
            return true;

        // Special Cases
        // p1, q1 and p2 are colinear and p2 lies on segment p1q1
        if (o1 == 0 && onSegment(p1, p2, q1)) return true;

        // p1, q1 and p2 are colinear and q2 lies on segment p1q1
        if (o2 == 0 && onSegment(p1, q2, q1)) return true;

        // p2, q2 and p1 are colinear and p1 lies on segment p2q2
        if (o3 == 0 && onSegment(p2, p1, q2)) return true;

        // p2, q2 and q1 are colinear and q1 lies on segment p2q2
        return !!(o4 == 0 && onSegment(p2, q1, q2));

         // Doesn't fall in any of the above cases
    }


    var Geometry = {
        lineIntersectCircle: function (line, circle) {
            var pointB = line.pointB;
            var pointA = line.pointA;
            if (pointA.x == pointB.x && pointA.y == pointB.y) {
                return Math.pow(pointA.x - circle.x, 2) + Math.pow(pointA.y - circle.y, 2) == circle.radius * circle.radius ? [pointA] : [];
            } else {
                var baX = pointB.x - pointA.x;
                var baY = pointB.y - pointA.y;
                var caX = circle.x - pointA.x;
                var caY = circle.y - pointA.y;
                var a = baX * baX + baY * baY;
                var bBy2 = baX * caX + baY * caY;
                var c = caX * caX + caY * caY - circle.radius * circle.radius;
                var pBy2 = bBy2 / a;
                var q = c / a;
                var disc = pBy2 * pBy2 - q;
                if (disc <= 1.0E-4 && disc >= -1.0E-4) {
                    disc = 0;
                }

                if (disc < 0) {
                    return [];
                } else {
                    var tmpSqrt = Math.sqrt(disc);
                    var abScalingFactor1 = -pBy2 + tmpSqrt;
                    var abScalingFactor2 = -pBy2 - tmpSqrt;
                    var p1 = {x: pointA.x - baX * abScalingFactor1, y: pointA.y - baY * abScalingFactor1};
                    var out1 = Geometry.getMiddle(pointA, pointB, p1);
                    if (disc == 0) {
                        return out1 == p1 ? [p1] : [];
                    } else {
                        var p2 = {x: pointA.x - baX * abScalingFactor2, y: pointA.y - baY * abScalingFactor2};
                        var out2 = Geometry.getMiddle(pointA, pointB, p2);
                        return out1 == p1 ? (out2 == p2 ? [p1, p2] : [p1]) : (out2 == p2 ? [p2] : []);
                    }
                }
            }
        },
        canStand: function (zones, circle) {
            for(var i = 0; i < zones.length; i++) {
                var zone = zones[i];
                var center;
                var angle = zone.angle || 0;
                if (zone.angle === 0) {
                    center = null;
                } else {
                    center = {x: zone.x + zone.width / 2, y: zone.y + zone.height / 2};
                }
                var points = [
                    Geometry.translate(center, {x: zone.x, y: zone.y}, angle),
                    Geometry.translate(center, {x: zone.x + zone.width, y: zone.y}, angle),
                    Geometry.translate(center, {x: zone.x + zone.width, y: zone.y + zone.height}, angle),
                    Geometry.translate(center, {x: zone.x, y: zone.y + zone.height}, angle),
                ];
                var line = {pointA: points[0], pointB: points[1]};
                if (Geometry.lineIntersectCircle(line, circle).length === 0) {
                    line = {pointA: points[1], pointB: points[2]};
                    if (Geometry.lineIntersectCircle(line, circle).length === 0) {
                        line = {pointA: points[2], pointB: points[3]};
                        if (Geometry.lineIntersectCircle(line, circle).length === 0) {
                            line = {pointA: points[3], pointB: points[0]};
                            if (Geometry.lineIntersectCircle(line, circle).length !== 0) {
                                return false;
                            }
                        } else {
                            return false;
                        }
                    } else {
                        return false;
                    }
                } else {
                    return false;
                }
            }
            return true;
        },
        translate: function (rotationCenter, point, angle) {
            if (angle === 0) {
                return point;
            }
            var x = (Math.cos(angle) * (point.x - rotationCenter.x)) -
                (Math.sin(angle) * (point.y - rotationCenter.y)) +
                rotationCenter.x;
            var y = (Math.sin(angle) * (point.x - rotationCenter.x)) +
                (Math.cos(angle) * (point.y - rotationCenter.y)) +
                rotationCenter.y;
            return new Point(x, y);
        },
        getMiddle: function (pointA, pointB, pointC) {
            var ab = Geometry.getDistance(pointA, pointB);
            var bc = Geometry.getDistance(pointB, pointC);
            var ca = Geometry.getDistance(pointC, pointA);
            return ab >= bc && ab >= ca ? pointC : (ca >= ab && ca >= bc ? pointB : pointA);
        },
        getDistance: function (point1, point2) {
            if(!point1 || !point2) {
                console.log('lol');
            }
            var x1 = point1.x;
            var y1 = point1.y;
            var x2 = point2.x;
            var y2 = point2.y;
            return Math.sqrt(Math.pow(x2 - x1, 2.0) + Math.pow(y2 - y1, 2.0));
        },
        isPointInTriangle: function (p, p0, p1, p2) {
            var dX = p.x-p2.x;
            var dY = p.y-p2.y;
            var dX21 = p2.x-p1.x;
            var dY12 = p1.y-p2.y;
            var D = dY12*(p0.x-p2.x) + dX21*(p0.y-p2.y);
            var s = dY12*dX + dX21*dY;
            var t = (p2.y-p0.y)*dX + (p0.x-p2.x)*dY;
            if (D<0) return s<=0 && t<=0 && s+t>=D;
            return s>=0 && t>=0 && s+t<=D;
        },
        lineIntersectLine: function(lineA, lineB) {
            if(lineA.equals(lineB)) {
                return [lineA.a, lineA.b];
            } else {
                var x1 = lineA.a.x;
                var x2 = lineA.b.x;
                var y1 = lineA.a.y;
                var y2 = lineA.b.y;
                var x3 = lineB.a.x;
                var x4 = lineB.b.x;
                var y3 = lineB.a.y;
                var y4 = lineB.b.y;
                var xv12 = x2 - x1;
                var xv13 = x3 - x1;
                var xv14 = x4 - x1;
                var yv12 = y2 - y1;
                var yv13 = y3 - y1;
                var yv14 = y4 - y1;
                var xv31 = x1 - x3;
                var xv32 = x2 - x3;
                var xv34 = x4 - x3;
                var yv31 = y1 - y3;
                var yv32 = y2 - y3;
                var yv34 = y4 - y3;
                var v1 = xv34 * yv31 - yv34 * xv31;
                var v2 = xv34 * yv32 - yv34 * xv32;
                var v3 = xv12 * yv13 - yv12 * xv13;
                var v4 = xv12 * yv14 - yv12 * xv14;
                if(v1 * v2 <= 0 && v3 * v4 <= 0) {
                    var A1 = y2 - y1;
                    var A2 = y4 - y3;
                    var B1 = x1 - x2;
                    var B2 = x3 - x4;
                    var C1 = x1 * A1 + y1 * B1;
                    var C2 = x3 * A2 + y3 * B2;
                    var D = A1 * B2 - B1 * A2;
                    if(D != 0) {
                        var start1 = C1 * B2 - B1 * C2;
                        var Dy = A1 * C2 - C1 * A2;
                        return [new Point(start1 / D, Dy / D)];
                    } else if(B1 == B2 && B1 == 0 || A1 / B1 == A2 / B2 && C1 / B1 == C2 / B2) {
                        var start = Geometry.getMiddle(lineA.a, lineA.b, lineB.a);
                        var end;
                        if(start == lineA.a) {
                            end = Geometry.getMiddle(lineA.b, lineB.a, lineB.b);
                        } else if(start == lineA.b) {
                            end = Geometry.getMiddle(lineA.a, lineB.a, lineB.b);
                        } else {
                            end = Geometry.getMiddle(lineA.a, lineA.b, lineB.b);
                        }
                        return [start, end];
                    } else {
                        return [];
                    }
                } else {
                    return [];
                }
            }
        },
        truncate: function(angle) {
            while(angle > dPi) {
                angle -= dPi;
            }
            while(angle < 0) {
                angle += dPi;
            }
            return angle;
        },
        lineHasIntersections: function(lineA, lineB) {
            return doIntersect(lineA.a, lineA.b, lineB.a, lineB.b);
        }
    };
    return Geometry
});
