Engine.define('LandingLight', [], function(){

    function LandingLight(x, y, length, angle, color, directionToZero){
        this.x = x;
        this.y = y;
        this.length = length;
        this.color = color;
        this.angle = angle;
        this.directionToZero = directionToZero === undefined ? true : directionToZero;
        this.alpha = 1;
        this.alphaSteps = 10;
        this.alphaCounter = 0;
    }

    LandingLight.prototype.draw = function(context) {
        context.save();
        context.translate(this.x, this.y);
        context.rotate(this.angle);

        var length = this.directionToZero ? 0 : this.length;
        var dash = 10;
        var whitespace = 5;
        context.beginPath();
        context.strokeStyle = this.color;
        var alpha = this.alpha;
        while(this.directionToZero ? length < this.length : length > 0) {

            context.beginPath();
            var dashLength;
            if(this.directionToZero) {
                dashLength = length + dash < this.length ? dash : this.length - length;
            } else {
                dashLength = length - dash > 0 ? dash : length;
            }

            context.moveTo(length, 0);
            if(this.directionToZero) {
                length += dashLength;
            } else {
                length -= dashLength;
            }
            context.lineTo(length, 0);
            if(this.directionToZero) {
                length += whitespace;
            } else {
                length -= whitespace;
            }

            alpha -= 0.2;
            context.globalAlpha = alpha;
            if (alpha <= 0.001) {
                alpha = 1;
            }

            context.stroke();
            context.closePath();
        }
        this.alphaCounter++;
        if(this.alphaCounter > this.alphaSteps) {
            this.alphaCounter = 0;
            this.alpha -= 0.2;
            if (this.alpha <= 0.001) {
                this.alpha = 1;
            }
        }
        context.globalAlpha = 1.0;
        context.restore();
    };

    return LandingLight;

});