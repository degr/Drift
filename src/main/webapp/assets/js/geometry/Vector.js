Engine.define("Vector", function(){
    function Vector(x,y){
        this.x = x;
        this.y = y;
    }

    Vector.prototype.plus = function(vector){
        if(!(vector instanceof  Vector)) {
            throw "Invalid arguments exception";
        }
        return new Vector(this.x + vector.x, this.y + vector.y);
    };

    Vector.prototype.append = function(vector) {
        if(!(vector instanceof  Vector)) {
            throw "Invalid arguments exception";
        }
        this.x += vector.x;
        this.y += vector.y;
    };
    return Vector;
});
