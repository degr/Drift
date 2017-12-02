Engine.define("SpaceShip", ['Vector', 'BaseObject', 'Gun', 'RelativePointsObject', 'Point', 'Geometry', 'Explosion', 'Bullet'], function() {

    var RelativePointsObject = Engine.require("RelativePointsObject");
    var BaseObject = Engine.require("BaseObject");
    var Explosion = Engine.require("Explosion");
    var Geometry = Engine.require("Geometry");
    var Vector = Engine.require("Vector");
    var Bullet = Engine.require("Bullet");
    var Point = Engine.require("Point");
    var Gun = Engine.require("Gun");

    function SpaceShip(x,y, context){
        RelativePointsObject.apply(this, [x, y]);
        this.points = [
            new Point(-12, 12),
            new Point(15, 0),
            new Point(-12, -12)
        ];
        this.invincible = true;
        this.vector = new Vector(0,0);
        this.turnToLeft = false;
        this.turnToRight = false;
        this.hasAcceleration = false;
        this.guns = [new Gun(0, 0)];
        this.fireStarted = false;
        this.positionWasUpdated = false;
        this.angleWasUpdated = false;
        this.alive = true;
        this.context = context;
    }
    SpaceShip.prototype = Object.create(RelativePointsObject.prototype);
    SpaceShip.prototype.constructor = SpaceShip;

    SpaceShip.prototype.update = function(){
        if(this.positionWasUpdated) {
            this.positionWasUpdated = false;
        } else {
            if (this.hasAcceleration) {
                var acceleration = 0.1;
                var x = Math.cos(this.angle) * acceleration;
                var y = Math.sin(this.angle) * acceleration;
                var vector = new Vector(x, y);
                this.vector.append(vector);
            }
        }

        this.x+=this.vector.x;
        this.y+=this.vector.y;

        if(this.angleWasUpdated) {
            this.angleWasUpdated = false;
        } else {
            if (this.turnToLeft) {
                this.angle -= 0.07;
            }
            if (this.turnToRight) {
                this.angle += 0.07;
            }
        }

        if(this.fireStarted) {
            for(var i = 0; i< this.guns.length; i++) {
                var gun = this.guns[i];
                if(gun.canFire()) {
                    if(this.context.freeSpace) {
                        gun.fire();
                    } else {
                        var bullet = gun.fire();
                        bullet.correct(this);
                        var angle = Geometry.truncate(this.angle + Math.PI);
                        this.vector.append(new Vector(Math.cos(angle) * 0.2, Math.sin(angle) * 0.2));
                        this.context.space.objects.push(bullet);
                    }
                }
            }
        }
    };

    SpaceShip.prototype.listen = function(keyDownListener, keyUpListener){
        this.keyDownListener = keyDownListener;
        this.keyUpListener = keyUpListener;
        document.body.addEventListener('keydown', this.keyDownListener);
        document.body.addEventListener('keyup', this.keyUpListener);
    };

    SpaceShip.prototype.isAlive = function() {
        return this.alive;
    };

    SpaceShip.prototype.unListen = function(){
        if(this.keyDownListener) {
            document.body.removeEventListener('keydown', this.keyDownListener);
            this.keyDownListener = null;
        }
        if(this.keyUpListener) {
            document.body.removeEventListener('keyup', this.keyUpListener);
            this.keyUpListener = null;
        }
    };
    SpaceShip.prototype.draw = function(context, appContext){
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
        context.textAlign = 'center';
        context.strokeText(parseInt(this.x) + '/' + parseInt(this.y), 0, 20);
        context.beginPath();
        if(this.invincible) {
            context.strokeStyle = "yellow";
            context.arc(0, 0, 30, 0, Math.PI * 2);
            context.stroke();
            context.closePath();
            context.beginPath();
        }

        context.strokeStyle = "lightblue";
        context.moveTo(0, 0);
        context.lineTo(this.vector.x * 5, this.vector.y * 5);
        context.stroke();

        context.rotate(this.angle);
        context.strokeStyle = "#59f741";
        this.placePoints(context);
        for(var i = 0; i < this.guns.length; i++) {
            var gun = this.guns[i];
            gun.draw(context);
        }
        if(this.hasAcceleration) {
            context.translate(-20, 0);
            var explosion = new Explosion(0, 0, this.vector, 10, 10, 'steelblue');
            explosion.drawWithoutShift(context, appContext);
        }
        context.restore();
    };

    SpaceShip.prototype.hasImpact = function(object) {
        return false;
        if(object instanceof Bullet && object.ship === this.id) {
            return false;
        } else if(object instanceof Gun) {
            return false;
        } else {
            return BaseObject.prototype.hasImpact.apply(this, arguments);
        }
    };

    SpaceShip.prototype.onImpact = function(object) {
        this.alive = false;
        if(this.onDestroy) {
            this.onDestroy();
        }
        return [new Explosion(this.x, this.y, this.vector, 30)];
    };

    return SpaceShip;
});