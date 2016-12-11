Engine.define("Explosion", ["BaseObject", 'ExplosionRay'], function () {

    var ExplosionRay = Engine.require('ExplosionRay');
    var BaseObject = Engine.require('BaseObject');

    function Explosion(x, y, vector, radius, life) {
        BaseObject.apply(this, [x, y]);
        this.vector = vector;
        this.life = life || 100;
        this.rays = [];
        var limit = 5 + Math.ceil(Math.random() * 10);
        while(this.rays.length < limit) {
            this.rays.push(new ExplosionRay(radius));
        }
    }
    Explosion.prototype = Object.create(BaseObject.prototype);
    Explosion.prototype.constructor = Explosion;

    Explosion.prototype.update = function(appContext) {
        this.life--;
        this.x += this.vector.x;
        this.y += this.vector.y;
        var l = this.rays.length;
        while(l--) {
            this.rays[l].update(appContext);
        }
    };

    Explosion.prototype.isAlive = function(){
        return this.life > 0;
    };

    Explosion.prototype.draw = function(context, appContext) {
        var shift = appContext.shift;
        context.save();
        context.translate(this.x + shift.x, this.y + shift.y);
        var length = this.rays.length;
        while(length--) {
            this.rays[length].draw(context);
        }
        context.restore();
    };

    return Explosion;
});