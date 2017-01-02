Engine.define("AsteroidFactory", 'Asteroid', function(){

    var Asteroid = Engine.require("Asteroid");

    function AsteroidFactory() {

    }

    AsteroidFactory.prototype.create = function(maxX, maxY, active) {
        return new Asteroid(Math.random() * maxX, Math.random() * maxY, null, active);
    };

    AsteroidFactory.prototype.createMulty = function(limit, maxX, maxY, active) {
        if(limit < 1) {
            throw "Invalid arguments exception";
        }
        var out = [];
        while(limit--) {
            out.push(this.create(maxX, maxY, active));
        }
        return out;
    };

    return AsteroidFactory;
});