Engine.define("Bullet", ['Vector', 'Geometry', 'BaseObject', 'Explosion', 'Point'], function() {

    var BaseObject = Engine.require("BaseObject");
    var Explosion = Engine.require("Explosion");
    var Geometry = Engine.require("Geometry");
    var Vector = Engine.require("Vector");
    var Point = Engine.require("Point");

    function Bullet(x, y, angle) {
        BaseObject.apply(this, arguments);
        this.vector = null;
        var me = this;
        this.ship = null;
        this.alive = true;
        setTimeout(function(){
            me.alive = false;
        }, 3000);
    }
    Bullet.prototype = Object.create(BaseObject.prototype);
    Bullet.constructor = Bullet;


    Bullet.prototype.correct = function(spaceShip) {
        //this.points = [new Point(oldX, ol
        this.x += spaceShip.x;
        this.y += spaceShip.y;
        var translated = Geometry.translate(spaceShip, this, spaceShip.angle);
        this.x = translated.x;
        this.y = translated.y;
        this.angle += spaceShip.angle;

        this.points = [
            new Point(this.x, this.y),
            new Point(this.x + 5 * Math.cos(this.angle), this.y + 5 * Math.sin(this.angle))
        ];
        this.oldPoint = this.points[0];
        this.vector = new Vector(
            Bullet.speed * Math.cos(this.angle),
            Bullet.speed * Math.sin(this.angle)
        );
        this.vector.append(spaceShip.vector);
        this.ship = spaceShip.id;
    };

    Bullet.speed = 5;

    Bullet.prototype.draw = function(context, appContext) {
        var shift = appContext.shift;
        context.save();
        context.translate(this.x + shift.x, this.y+ shift.y);
        context.rotate(this.angle);
        context.beginPath();
        context.strokeStyle = "white";

        context.moveTo(-3, 0);
        context.lineTo(2, 0);
        context.stroke();
        context.restore();

        context.beginPath();
        context.strokeStyle = "red";
        context.moveTo(this.points[0].x+ shift.x, this.points[0].y+ shift.y);
        context.lineTo(this.points[1].x+ shift.x, this.points[1].y+ shift.y);
        context.stroke();



    };
    Bullet.prototype.update = function() {
        this.x += this.vector.x;
        this.y += this.vector.y;
        this.oldPoint = this.points[0].clone();
        this.points[0].x += this.vector.x;
        this.points[0].y += this.vector.y;
        this.points[1].x += this.vector.x;
        this.points[1].y += this.vector.y;
    };

    Bullet.prototype.isAlive = function() {
        return this.alive;
    };

    Bullet.prototype.hasImpact = function(object) {
        if(object.id !== this.ship) {
            return BaseObject.prototype.hasImpact.apply(this, arguments);
        } else {
            return false
        }
    };

    Bullet.prototype.onImpact = function(object) {
        this.alive = false;
        return [new Explosion(this.x, this.y, this.vector, 8, 60)];
    };

    Bullet.prototype.getPoints = function(){
        if(!this.oldPoint || !this.points[1]) {
            return [];
        } else {
            return [this.oldPoint, this.points[1]];
        }
    };

    return Bullet;
});