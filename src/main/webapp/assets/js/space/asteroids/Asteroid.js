Engine.define("Asteroid", ["Vector", 'Point', 'Explosion', 'RelativePointsObject', 'MassUtils'], function(){

    var RelativePointsObject = Engine.require("RelativePointsObject");
    var Explosion = Engine.require("Explosion");
    var MassUtils = Engine.require("MassUtils");
    var Vector = Engine.require("Vector");
    var Point = Engine.require("Point");

    function Asteroid(x,y, points, active, angle){
        RelativePointsObject.apply(this, [x, y, angle]);

        this.active = active;
        this.vector = new Vector(
            Math.random() * 2 - 1,
            Math.random() * 2 - 1
        );

        if(points) {
            this.points = points;
        } else {
            this.points = this.createPoints();
        }
        this.rotationSpeed = Math.random() * 0.2 - 0.1;
        this.alive = true;
    }
    Asteroid.prototype = Object.create(RelativePointsObject.prototype);
    Asteroid.prototype.constructor = Asteroid;

    Asteroid.prototype.isAlive = function(){
        return this.alive;
    };
    Asteroid.prototype.update = function(){
        this.x += this.vector.x;
        this.y += this.vector.y;
        this.angle += this.rotationSpeed;
    };

    Asteroid.prototype.draw = function(context, appContext){
        var shift = appContext.shift;
        var positionX = this.x + shift.x;
        var positionY = this.y + shift.y;
        if(positionX < 0 || positionX > appContext.space.width) {
            return;
        } else if(positionY < 0 || positionY > appContext.space.width) {
            return;
        }

        context.save();
        context.translate(positionX, positionY);
        context.rotate(this.angle);
        context.fillStyle = "#9e9e9e";
        this.placePoints(context);
        context.fill();


        context.rotate(-this.angle);
        context.fillStyle = "red";
        context.fillRect(-2, -2, 4, 4);
        context.strokeText(this.id, 0, -15);
        context.restore();
    };

    Asteroid.prototype.hasImpact = function(object) {
        if(object instanceof Explosion || object instanceof Asteroid) {
            return false
        } else {
            return RelativePointsObject.prototype.hasImpact.apply(this, arguments);
        }
    };

    Asteroid.prototype.onImpact = function(object) {
        this.alive = false;
        var out;
        if(this.active && this.points.length > 3) {
            out = this.createNewAsteroids();
        } else {
            out = [];
        }
        out.push(new Explosion(this.x, this.y, this.vector, 30));
        return out;
    };

    Asteroid.prototype.createNewAsteroids = function() {
        var firstSize = 3;
        var start = 0;
        var totalSquare = MassUtils.getSquare(this.points);
        var points = this.points;

        while (true) {
            var p1 = [];
            var p2 = [];
            var i;
            var limit = firstSize + start;
            var j = 0;
            for (i = start; i < limit; i++) {
                if (i < points.length) {
                    p1[j] = points[i];
                } else {
                    p1[j] = points[i - points.length];
                }
                j++;
            }
            limit = points.length + start;
            for (i = start + 2; i <= limit; i++) {
                var index;
                if (i < points.length) {
                    index = i;
                } else {
                    index = i - points.length;
                }
                p2[i - start - 2] = points[index];
            }
            var square1 = MassUtils.getSquare(p1);
            var square2 = MassUtils.getSquare(p2);
            var check = totalSquare - square1 - square2;
            if (Math.abs(check) < 0.00001) {
                return this.processByTwoPoints(p1, p2);
            } else {
                start++;
            }
        }
    };

    Asteroid.prototype.setToCenter = function(points, centerOfMass) {
        for (var i = 0; i < points.length; i++) {
            var p = points[i];
            points[i] = new Point(p.x - centerOfMass[0], p.y - centerOfMass[1]);
        }
        return points;
    };

    Asteroid.prototype.processByTwoPoints = function(p1, p2) {
        var out = [];
        var aCenter = MassUtils.getTriangleCenter(p1[0], p1[1], p1[2]);
        p1 = this.setToCenter(p1, aCenter);
        out[0] = new Asteroid(this.x + aCenter[0], this.y + aCenter[1], p1, this.active, this.angle);

        aCenter = MassUtils.getCenterOfMass(p2, new Point(0, 0));
        p2 = this.setToCenter(p2, aCenter);
        out[1] = new Asteroid(this.x + aCenter[0], this.y + aCenter[1], p2, this.active, this.angle);
        return out;
    };

    Asteroid.prototype.createPoints = function(){
        var limit = Math.ceil(Math.random() * 7) + 3;
        var sector = Math.PI * 2 / limit;
        var points = [];
        while (limit-- > 0) {
            var angle = (Math.random() * (sector)) + sector * limit;
            var distance = Math.random() * 90;
            points[limit] = new Point(
                Math.cos(angle) * distance,
                Math.sin(angle) * distance
            );
        }
        var centerOfMass = MassUtils.getCenterOfMass(points, new Point(0, 0));
        return this.setToCenter(points, centerOfMass);
    };

    return Asteroid;
});