Engine.define('Sandbox', ['Space', 'SpaceShip', 'AsteroidFactory'], function(){

    var AsteroidFactory = Engine.require('AsteroidFactory');
    var SpaceShip = Engine.require('SpaceShip');
    var Space = Engine.require('Space');

    var Sandbox = {
        clb: null,
        started: false,
        start: function(clb) {
            if(Sandbox.started) {
                throw "Can't start sandbox thread, becaus previous is not stopped";
            }
            Sandbox.started = true;

            var maxX = 5000;
            var maxY = 5000;

            this.clb = clb;
            this.space = new Space(maxX, maxY);
            var spaceShip = new SpaceShip(maxX / 2, maxY / 2, {
                space: this.space,
                socket: {send: function(){

                }}
            });
            spaceShip.listen(function(obj){
                Sandbox.space.objects.push(obj)
            });
            spaceShip.onDestroy = function(){
                clb();
            };
            this.space.spaceShip = spaceShip;
            this.space.objects.push(spaceShip);
            var asteroidFactory = new AsteroidFactory();
            var asteroids = asteroidFactory.createMulty(150, maxX, maxY, true);
            asteroids.forEach(function(asteroid){
                Sandbox.space.objects.push(asteroid);
            });
            this.space.start();
        },
        stop: function(){
            Sandbox.started = false;
            this.space.stop();
            if(Sandbox.clb) {
                Sandbox.clb();
                Sandbox.clb = null;
            }
        }
    };


    return Sandbox;
});