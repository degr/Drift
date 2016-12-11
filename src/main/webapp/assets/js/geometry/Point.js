Engine.define("Point", function(){
    function Point(x, y) {
        this.x = x;
        this.y = y;
    }

    Point.prototype.toString = function() {
        return "(" + this.x + "|" + this.y + ")";
    };

    Point.prototype.equals = function(obj) {
        if(!(obj instanceof Point)) {
            return false;
        } else {
            return obj.x == this.x && obj.y == this.y;
        }
    };

    Point.prototype.clone = function() {
        return new Point(this.x, this.y);
    };
    return Point
});
