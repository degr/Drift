Engine.define("Startup", ['Dom', "ScreenUtils", "LayeredCanvas", 'SpaceShip', 'CanvasWindow', 'AsteroidFactory', 'Geometry'], function () {

    var AsteroidFactory = Engine.require('AsteroidFactory');
    var LayeredCanvas = Engine.require('LayeredCanvas');
    var CanvasWindow = Engine.require('CanvasWindow');
    var ScreenUtils = Engine.require('ScreenUtils');
    var SpaceShip = Engine.require('SpaceShip');
    var Geometry = Engine.require('Geometry');
    var Dom = Engine.require('Dom');

    var Startup = {
        width: 1500,
        height: 1500,
        canvas: null,
        spaceShip: null,
        canvasWindow: null,
        objects: [],
        appContext: {},
        listeners: {
            onresize: function () {
                var screen = ScreenUtils.window();
                Startup.appContext.screen = screen;
                Startup.canvas.resize(screen.width, screen.height);
                Startup.canvasWindow.resize(screen);
            }
        },
        start: function () {
            var screen = ScreenUtils.window();
            var me = this;
            this.canvas = new LayeredCanvas(2, screen.width, screen.height);
            this.canvasWindow = new CanvasWindow(Startup.width, Startup.height);
            Dom.addListeners(Startup.listeners);
            this.spaceShip = new SpaceShip(60, 60);
            this.spaceShip.listen(function (item) {
                me.objects.push(item);
            });
            this.objects.push(this.spaceShip);
            this.appContext.startup = this;
            this.appContext.screen = screen;
            document.body.appendChild(this.canvas.container);
            var asteroidFactory = new AsteroidFactory();
            this.objects = this.objects.concat(asteroidFactory.createMulty(10, this.width, this.height));
            setInterval(function () {
                Startup.run()
            }, 20);
        },
        run: function () {
            var canvas = this.canvas;
            var context = canvas.getContext(0);
            context.clearRect(0, 0, canvas.width, canvas.height);
            var length = this.objects.length;
            var shift = this.canvasWindow.getShift(this.spaceShip);
            this.appContext.shift = shift;
            context.beginPath();
            context.strokeStyle = "red";
            context.lineWidth = 10;
            context.rect(shift.x, shift.y, Startup.width, Startup.height);
            context.stroke();
            context.lineWidth = 1;

            while (length--) {
                var obj = this.objects[length];
                if (obj.isAlive()) {
                    obj.update();
                    if(obj.x > Startup.width) {
                        obj.x = 0;
                    } else if(obj.x < 0) {
                        obj.x = Startup.width;
                    }
                    if(obj.y > Startup.height) {
                        obj.y = 0;
                    } else if(obj.y < 0) {
                        obj.y = Startup.height;
                    }
                    obj.draw(context, this.appContext);
                    Startup.calculateImpacts(obj);
                } else {
                    this.objects.splice(length, 1);
                }
            }
        },
        stop: function () {
            Dom.removeListeners(Startup.listeners);
            this.spaceShip.unListen();
        },
        calculateImpacts: function (obj) {
            var length = this.objects.length;

            while(length--) {
                if(this.objects[length] !== obj) {
                    var distance = Geometry.getDistance(obj, this.objects[length]);
                    if (distance < 300) {
                        var impact = obj.hasImpact(this.objects[length]);
                        if (impact) {
                            var imp1 = obj.onImpact(this.objects[length], this.appContext);
                            var imp2 = this.objects[length].onImpact(obj, this.appContext);
                            this.objects = this.objects.concat(imp1, imp2);
                        }
                    }
                }
            }
        }
    };


    return Startup;
});