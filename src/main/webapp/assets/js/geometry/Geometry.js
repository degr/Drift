Engine.define('Geometry', ['Point', 'Line'], function () {

    var Point = Engine.require("Point");
    var Line = Engine.require("Line");


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
            var x1 = point1.x;
            var y1 = point1.y;
            var x2 = point2.x;
            var y2 = point2.y;
            return Math.sqrt(Math.pow(x2 - x1, 2.0) + Math.pow(y2 - y1, 2.0));
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
        lineHasIntersections: function(lineA, lineB) {
            if(lineA.equals(lineB)) {
                return true;
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
                        return true;
                    } else if(B1 == B2 && B1 == 0 || A1 / B1 == A2 / B2 && C1 / B1 == C2 / B2) {
                        return true;
                    } else {
                        return false
                    }
                } else {
                    return false;
                }
            }
        }

    };
    return Geometry
});