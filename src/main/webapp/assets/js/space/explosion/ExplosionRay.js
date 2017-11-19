Engine.define("ExplosionRay", 'Point', function(){

    var Point = Engine.require("Point");

    function ExplosionRay(radius, color) {
        this.color = color ? color : (Math.random() > 0.5 ? 'red' : 'yellow');//'#' + Math.floor(Math.random()*16777215).toString(16);
        this.grow = Math.random() > 0.5;
        this.radius = radius;
        this.angle = Math.random() * Math.PI * 2;
        this.length = Math.random() * radius;
    }

    ExplosionRay.zero = new Point(0, 0);

    ExplosionRay.prototype.update = function() {
       if(this.grow) {
           if(this.length < this.radius) {
               this.length ++;
           } else {
               this.grow = false;
               this.length--;
           }
       } else {
           if(this.length > 0) {
               this.length--;
           } else {
               this.grow = true;
               this.length++;
           }
       }
    };

    ExplosionRay.prototype.draw = function(context) {
        var x = Math.cos(this.angle) * this.length;
        var y = Math.sin(this.angle) * this.length;
        context.beginPath();
        context.moveTo(0, 0);
        context.lineTo(x, y);
        context.strokeStyle = this.color;
        context.stroke();
        context.closePath();
    };

    return ExplosionRay;
});