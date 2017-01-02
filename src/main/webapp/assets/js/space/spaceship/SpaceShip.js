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
        this.alive = true;
        this.context = context;
    }
    SpaceShip.prototype = Object.create(RelativePointsObject.prototype);
    SpaceShip.prototype.constructor = SpaceShip;

    SpaceShip.prototype.update = function(){
        if(this.hasAcceleration){
            var acceleration = 0.1;
            var x = Math.cos(this.angle)*acceleration;
            var y = Math.sin(this.angle)*acceleration;
            var vector = new Vector(x, y);
            this.vector.append(vector);
        }
        if(this.turnToLeft){
            this.angle-=0.07;
        }
        if(this.turnToRight){
            this.angle+=0.07;
        }

        this.x+=this.vector.x;
        this.y+=this.vector.y;
        if(this.fireStarted) {
            for(var i = 0; i< this.guns.length; i++) {
                var gun = this.guns[i];
                if(gun.canFire()) {
                    var bullet = gun.fire();
                    bullet.correct(this);
                    var angle = Geometry.truncate(this.angle + Math.PI);
                    this.vector.append(new Vector(Math.cos(angle) * 0.2, Math.sin(angle)* 0.2));
                    this.context.space.objects.push(bullet);
                }
            }
        }
    };

    SpaceShip.prototype.listen = function(){
        var me = this;
        this.keyDownListener = function(event) {
            if(me.invincible) {
                me.invincible = false;
            }
            var keyCode = event.keyCode;
            if(keyCode == 39){
                event.preventDefault();
                if(!me.turnToLeft) {
                    me.context.socket.send("turn:1")
                } else {
                    me.context.socket.send("turn:0")
                }
                me.turnToRight = true;
            } else if(keyCode == 37){
                event.preventDefault();
                if(!me.turnToRight) {
                    me.context.socket.send("turn:-1")
                } else {
                    me.context.socket.send("turn:0")
                }
                me.turnToLeft = true;
            } else if(keyCode == 38){
                event.preventDefault();
                me.context.socket.send("accelerate:1");
                me.hasAcceleration = true;
            } else if(keyCode == 40){
                event.preventDefault();
            } else if(keyCode == 32) {
                me.fireStarted = true;
                me.context.socket.send("fire:1")
            }
        };
        this.keyUpListener = function(event) {
            var keyCode = event.keyCode;
            console.log(keyCode);
            if(keyCode == 39){
                event.preventDefault();
                me.turnToRight = false;
                if(me.turnToLeft) {
                    me.context.socket.send("turn:-1")
                } else {
                    me.context.socket.send("turn:0")
                }
            } else if(keyCode == 37){
                event.preventDefault();
                me.turnToLeft = false;
                if(me.turnToRight) {
                    me.context.socket.send("turn:1")
                } else {
                    me.context.socket.send("turn:0")
                }
            } else if(keyCode == 38){
                event.preventDefault();
                me.hasAcceleration = false;
                me.context.socket.send("accelerate:0");
            } else if(keyCode == 32) {
                me.context.socket.send("fire:0");
                me.fireStarted = false;
            }
        };
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
        context.restore();
    };

    SpaceShip.prototype.onImpact = function(object, appContext) {
        if(object instanceof Bullet) {
            if(object.ship === this) {
                return [];
            }
        } else if(object instanceof Gun) {
            return [];
        }
        this.alive = false;
        if(this.onDestroy) {
            this.onDestroy();
        }
        return [new Explosion(this.x, this.y, this.vector, 30)];
    };

    return SpaceShip;
});