Engine.define('FakeServer', ['AsteroidFactory'], function(){


    function FakeServer(clb) {
        var asteroidFactory = new AsteroidFactory();
        this.objects = this.objects.concat(asteroidFactory.createMulty(10, this.width, this.height));
        this.started = false;
    }

    FakeServer.prototype.onTick = function(){

    };

    FakeServer.prototype.onMessage = function(message){

    };

    return FakeServer;
});