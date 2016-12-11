Engine.define("Line", function(){
    function Line(a, b) {
        this.a = a;
        this.b = b;
    }

    Line.prototype.equals = function(obj) {
        if(!(obj instanceof Line)) {
            return false;
        } else {
            return obj.a.equals(this.a) && obj.b.equals(this.b) ||
                obj.b.equals(this.a) && obj.a.equals(this.b);
        }
    };

    Line.prototype.toString = function() {
        return "Line " + this.a + " " + this.b;
    };

    return Line;
});