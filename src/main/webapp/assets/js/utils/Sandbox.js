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
                freeSpace: false,
                space: this.space
            });
            var keyDownListener = function(event) {
                if(spaceShip.invincible) {
                    spaceShip.invincible = false;
                }
                var keyCode = event.keyCode;
                if(keyCode === 39){
                    event.preventDefault();
                    spaceShip.turnToRight = true;
                } else if(keyCode === 37){
                    event.preventDefault();
                    spaceShip.turnToLeft = true;
                } else if(keyCode === 38){
                    event.preventDefault();
                    spaceShip.hasAcceleration = true;
                } else if(keyCode === 40){
                    event.preventDefault();
                } else if(keyCode === 32) {
                    spaceShip.fireStarted = true;
                }
            };
            var keyUpLitener = function(event){
                var keyCode = event.keyCode;
                if(keyCode === 39){
                    event.preventDefault();
                    spaceShip.turnToRight = false;
                } else if(keyCode === 37){
                    event.preventDefault();
                    spaceShip.turnToLeft = false;
                } else if(keyCode === 38){
                    event.preventDefault();
                    spaceShip.hasAcceleration = false;
                } else if(keyCode === 32) {
                    spaceShip.fireStarted = false;
                }
            };
            spaceShip.listen(keyDownListener, keyUpLitener);

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