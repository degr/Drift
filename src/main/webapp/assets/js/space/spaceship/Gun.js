Engine.define("Gun", ['Vector', 'Bullet'], function() {

    var Vector = Engine.require("Vector");
    var Bullet = Engine.require("Bullet");

    function Gun(x,y, angle, color){
        this.x = x;
        this.y = y;
        this.angle = angle || 0;
        this.color = color || 'red';
        this.reload = false;
    }

    Gun.prototype.draw = function(context) {
        context.strokeStyle = this.color;
        context.save();
        context.lineWidth = 1;
        context.rotate(this.angle);
        context.beginPath();
        context.moveTo(this.x + 5, this.y);
        context.lineTo(this.x - 5, this.y);
        context.stroke();
        context.closePath();
        context.restore();
    };

    Gun.prototype.canFire = function() {
        return !this.reload;
    };

    Gun.prototype.fire = function() {
        if(!this.reload) {
            this.reload = true;
            var me = this;
            setTimeout(function(){
                me.reload = false;
            }, 1000);
            return new Bullet(this.x, this.y, this.angle);
        } else {
            throw "Can't fire!";
        }
    };

    return Gun;
});