Engine.define('BaseObject', ['Geometry', 'Line'], function(){

    var Geometry = Engine.require('Geometry');
    var Line = Engine.require('Line');

    function BaseObject(x, y, angle, points) {
        this.points = points || [];
        this.x = x;
        this.y = y;
        this.angle = angle || 0;
        this.invincible = false;
    }

    BaseObject.prototype.placePoints = function(context, points) {
        var p = points || this.points;
        context.beginPath();
        context.moveTo(p[0].x, p[0].y);
        for(var i = 1; i < p.length;i++) {
            context.lineTo(p[i].x, p[i].y);
        }
        context.closePath();
        context.stroke();
    };

    BaseObject.appendLastPoint = function(input) {
        if(input.length > 2) {
            var out = [].concat(input);
            out.push(input[0]);
            return out;
        } else {
            return input;
        }
    };

    BaseObject.prototype.hasImpact = function(baseObject) {
        if(!(baseObject instanceof  BaseObject)) {
            throw "Invalid arguments exception";
        }
        if(baseObject.invincible || this.invincible || !baseObject.isAlive()){
            return false;
        }
        var thisPoints = BaseObject.appendLastPoint(this.getPoints());
        var thisLength = thisPoints.length;
        var thatPoints = BaseObject.appendLastPoint(baseObject.getPoints());
        while(--thisLength > 0) {
            var line1 = new Line(thisPoints[thisLength], thisPoints[thisLength - 1]);
            var thatLength = thatPoints.length;
            while(--thatLength > 0) {
                var line2 = new Line(thatPoints[thatLength], thatPoints[thatLength - 1]);
                if (Geometry.lineHasIntersections(line1, line2)) {
                    return true;
                }
            }
        }
        return false;
    };

    BaseObject.prototype.getPoints = function(){
        return this.points;
    };
    BaseObject.prototype.onImpact = function(object){

    };
    return BaseObject;
});