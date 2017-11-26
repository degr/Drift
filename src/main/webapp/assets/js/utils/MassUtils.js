Engine.define('MassUtils', ['Point'], function() {

    var Point = Engine.require('Point');

    var MassUtils = {

        getTriangleCenter: function(p, q, r) {
            return [
                (p.x + q.x + r.x) / 3,
                (p.y + q.y + r.y) / 3
            ]
        },
        getCenterOfMass(points, shift) {
            var l = points.length;
            var x = 0;
            var y = 0;
            var shiftX = shift.x;
            var shiftY = shift.y;
            var totalMass = 0;
            while(l-- > 0) {
                var p2 = points[l];
                var p3 = l === 0 ? points[points.length - 1] : points[l - 1];
                var center = MassUtils.getTriangleCenter(shift, p2, p3);
                //square = x1y2 + x2y3 + x3y1 – x1y3 – x2y1 – x3y2.
                var mass = MassUtils.getSquareByPoints(shiftX, shiftY, p2.x, p2.y, p3.x, p3.y);
                totalMass += mass;
                x += (center[0]) * mass;
                y += (center[1]) * mass;
            }

            return [
                x / totalMass,
                y / totalMass
            ]
        },
        getSquare: function(p) {
            var area = 0;
            var N = p.length;
            for(var i = 1; i+1<N; i++){
                area += MassUtils._getSquare(p[0].x, p[0].y, p[i].x, p[i].y, p[i + 1].x, p[i + 1].y);
            }
            return Math.abs(area/2.0);
        },
        getSquareByPoints: function(x1, y1, x2, y2, x3, y3) {
            return Math.abs(x1 * y2 + x2 * y3 + x3 * y1 - x1 * y3 - x2 * y1 - x3 * y2);
        },
        _getSquare(x1, y1, x2, y2, x3, y3) {
            return x1 * y2 + x2 * y3 + x3 * y1 - x1 * y3 - x2 * y1 - x3 * y2;
        },
        setToCenter(points, centerOfMass) {
            if(centerOfMass === undefined) {
                centerOfMass = MassUtils.getCenterOfMass(points, new Point(0, 0));
            }
            for (var i = 0; i < points.length; i++) {
                var p = points[i];
                points[i] = new Point(p.x - centerOfMass[0], p.y - centerOfMass[1]);
            }
            return points;
        }
    };
    return MassUtils;
});