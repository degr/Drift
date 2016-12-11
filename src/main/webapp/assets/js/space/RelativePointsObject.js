Engine.define('RelativePointsObject', ['BaseObject', 'Geometry', 'Point'], function(){

    var Point = Engine.require('Point');
    var Geometry = Engine.require('Geometry');
    var BaseObject = Engine.require('BaseObject');

    function RelativePointsObject(x, y, angle, points) {
        BaseObject.apply(this, arguments);
    }
    RelativePointsObject.prototype = Object.create(BaseObject.prototype);
    RelativePointsObject.prototype.constructor = RelativePointsObject;

    RelativePointsObject.zero = new Point(0, 0);

    RelativePointsObject.prototype.getPoints = function(){
        var me = this;
        return this.points.map(function(v){
            var p = Geometry.translate(RelativePointsObject.zero, v, me.angle);
            if(p === v) {
                p = p.clone();
            }
            p.x += me.x;
            p.y += me.y;
            return p;
        });
    };

    return RelativePointsObject;
});