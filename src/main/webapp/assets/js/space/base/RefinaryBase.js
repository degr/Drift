Engine.define('RefinaryBase', ['RelativePointsObject', 'Point', 'MassUtils'], function(){

    var RelativePointsObject = Engine.require('RelativePointsObject');
    var MassUtils = Engine.require('MassUtils');
    var Point = Engine.require('Point');

    function RefinaryBase(x, y, angle, points) {
        RelativePointsObject.apply(this, [x, y, angle || 0, points || RefinaryBase.generatePoints()]);
    }
    RefinaryBase.prototype = Object.create(RelativePointsObject.prototype);
    RefinaryBase.prototype.constructor = RefinaryBase;

    /**
     * @return Point[]
     */
    RefinaryBase.generatePoints = function(){
        var k = 20;
        var points = [
            new Point(-2 * k, -3 * k),
            new Point(-14 * k, -3 * k),
            new Point(-14 * k, 2 * k),
            new Point(-9 * k, 7 * k),
            new Point(3 * k, 7 * k),
            new Point(8 * k, 2 * k),
            new Point(8 * k, -7 * k),
            new Point(4 * k, -11 * k),
            new Point(-2 * k, -11 * k)
        ];
        return MassUtils.setToCenter(points);
    };

    RefinaryBase.prototype.draw = function(context, appContext){
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
        context.textAlign = 'center';
        context.strokeText('RefinaryBase', 0, 0);

        context.rotate(this.angle);
        context.strokeStyle = "#59f741";
        this.placePoints(context);
        context.restore();
    };

    RefinaryBase.prototype.update = function() {
        return null;
    };
    RefinaryBase.prototype.hasImpact = function() {
        return false;
    };
    RefinaryBase.prototype.isAlive = function() {
        return true;
    };

    return RefinaryBase;
});