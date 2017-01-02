Engine.define('AsteroidDust', function(){
    function AsteroidDust(x, y, objects) {
        this.x = x;
        this.y = y;
        this.globalObjects = objects;
        this.alive = true;
        this.radius = Math.random() * 150;
        this.initialRadius = this.radius;
        this.dust = [];
        var count = 10 * this.initialRadius;
        while(count--) {
            this.dust.push(new Dust(/*implement me*/))
        }
    }

    AsteroidDust.prototype.update = function() {
        this.radius--;
    };

    Asteroid.prototype.isAlive = function(){
        return this.alive;
    };

    AsteroidDust.prototype.draw = function(context, appContext){
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

    function Dust(x, y, stable, center) {
        this.x = x;
        this.y = y;
        this.center = center;
        this.y = y;
        this.stable = stable;
    }

    Dust.prototype.update = function(){
        if(this.stable)return;

    }

    return AsteroidDust;
});