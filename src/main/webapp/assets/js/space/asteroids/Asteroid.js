Engine.define("Asteroid", ["Vector", 'Point', 'Explosion', 'RelativePointsObject'], function(){

    var RelativePointsObject = Engine.require("RelativePointsObject");
    var Explosion = Engine.require("Explosion");
    var Vector = Engine.require("Vector");
    var Point = Engine.require("Point");

    function Asteroid(x,y, points, active){
        RelativePointsObject.apply(this, [x, y]);

        this.active = active;
        this.vector = new Vector(
            Math.random() * 2 - 1,
            Math.random() * 2 - 1
        );

        if(points) {
            this.points = points;
        } else {
            var limit = Math.ceil(Math.random() * 7) + 3;
            var sector = Math.PI * 2 / limit;
            while (limit--) {
                var angle = (Math.random() * (sector)) + sector * limit;
                var distance = Math.random() * 90;
                this.points.push(new Point(
                    Math.cos(angle) * distance,
                    Math.sin(angle) * distance
                ))
            }
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
        context.restore();
    };

    Asteroid.prototype.hasImpact = function(object) {
        if(object instanceof Asteroid) {
            return false
        } else {
            return RelativePointsObject.prototype.hasImpact.apply(this, arguments);
        }
    };

    Asteroid.prototype.onImpact = function(object) {
        this.alive = false;
        var out = [new Explosion(this.x, this.y, this.vector, 30)];
        if(this.active && this.points.length > 3) {
            var start = Math.ceil(this.points.length / 2);
            var p1 = [], i;
            for(i = 0; i <= start; i++) {
                p1.push(this.points[i]);
            }
            out.push(new Asteroid(this.x, this.y, p1, this.active));
            var p2 = [];
            for(i = start; i < this.points.length; i++) {
                p2.push(this.points[i]);
            }
            p2.push(this.points[0]);
            out.push(new Asteroid(this.x, this.y, p2, this.active));
        }
        return out;
    };

    return Asteroid;
});