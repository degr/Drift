Engine.define("AsteroidFactory", 'Asteroid', function(){

    var Asteroid = Engine.require("Asteroid");

    function AsteroidFactory() {

    }

    AsteroidFactory.prototype.create = function(max, min) {
        return new Asteroid(Math.random() * max, Math.random() * min);
    };

    AsteroidFactory.prototype.createMulty = function(limit, max, min) {
        if(limit < 1) {
            throw "Invalid arguments exception";
        }
        var out = [];
        while(limit--) {
            out.push(this.create(max, min));
        }
        return out;
    };

    return AsteroidFactory;
});