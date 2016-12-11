Engine.define("Bullet", ['Vector', 'Geometry', 'BaseObject', 'Point'], function() {

    var BaseObject = Engine.require("BaseObject");
    var Geometry = Engine.require("Geometry");
    var Vector = Engine.require("Vector");
    var Point = Engine.require("Point");

    function Bullet(x, y, angle) {
        BaseObject.apply(this, arguments);
        this.points = [];
        this.vector = null;
        this.oldX = -100;
        this.oldY = -100;
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
        this.x += spaceShip.x;
        this.y += spaceShip.y;
        var translated = Geometry.translate(spaceShip, this, spaceShip.angle);
        this.x = translated.x;
        this.y = translated.y;
        this.angle += spaceShip.angle;
        this.vector = new Vector(
            Bullet.speed * Math.cos(this.angle + Math.PI / 2),
            Bullet.speed * Math.sin(this.angle + Math.PI / 2)
        );
        this.vector.append(spaceShip.vector);
        this.ship = spaceShip;
    };

    Bullet.speed = 5;

    Bullet.prototype.draw = function(context, appContext) {
        var shift = appContext.shift;
        context.save();
        context.translate(this.x + shift.x, this.y+ shift.y);
        context.rotate(this.angle);
        context.beginPath();
        context.strokeStyle = "#43dff3";

        context.moveTo(0, -2);
        context.lineTo(0, 2);
        context.stroke();
        context.restore();
    };
    Bullet.prototype.update = function() {
        var oldX = this.x;
        var oldY = this.y;
        this.x += this.vector.x;
        this.y += this.vector.y;
        this.points = [new Point(oldX, oldY), new Point(this.x, this.y)]
    };

    Bullet.prototype.isAlive = function() {
        return this.alive;
    };

    Bullet.prototype.onImpact = function(object) {
        if(object !== this.ship) {
            this.alive = false;
        }
        return [];
    };

    return Bullet;
});