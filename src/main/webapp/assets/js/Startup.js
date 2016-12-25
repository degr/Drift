Engine.define("Startup", ['Dom','Popup', 'Timer', 'Profile', 'Bridge', 'WebSocketUtils', "ScreenUtils", "LayeredCanvas", 'SpaceShip', 'CanvasWindow', 'AsteroidFactory', 'Geometry'], function () {

    var AsteroidFactory = Engine.require('AsteroidFactory');
    var WebSocketUtils = Engine.require('WebSocketUtils');
    var LayeredCanvas = Engine.require('LayeredCanvas');
    var CanvasWindow = Engine.require('CanvasWindow');
    var ScreenUtils = Engine.require('ScreenUtils');
    var Geometry = Engine.require('Geometry');
    var Profile = Engine.require('Profile');
    var Bridge = Engine.require('Bridge');
    var Timer = Engine.require('Timer');
    var Popup = Engine.require('Popup');
    var Dom = Engine.require('Dom');

    var Startup = {
        width: 5000,
        height: 5000,
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
            var form = Dom.el('div', null, [
                Dom.el('div', null, [
                    Dom.el('input', {type: 'button', value: 'Sandbox', onclick: function(){

                    }, class: 'button-sandbox'}),
                    Dom.el('input', {type: 'button', value: 'Free Space', onclick: function(){

                    }, class: 'button-space'})
                ])
            ]);
            var modal = new Popup({
                controlMinimize: false,
                controlClose: false,
                title: Dom.el('h1', null, 'Please select game type')}
            );
            modal.setContent(form);
            modal.show();
            return;

            var context = {};
            var bridge = new Bridge(context);
            var socket = WebSocketUtils.getSocket(
                Profile.WS_URL,
                function(r){bridge.onOpen(r)},
                function(r){bridge.onMessage(r)},
                function(r){bridge.onClose(r)},
                function(r){bridge.onError(r)}
            );
            context.socket = socket;
            context.bridge = bridge;
            context.objects = Startup.objects;
            context.startup = Startup;

            var screen = ScreenUtils.window();
            var me = this;
            this.canvas = new LayeredCanvas(2, screen.width, screen.height);
            this.canvasWindow = new CanvasWindow(Startup.width, Startup.height);
            Dom.addListeners(Startup.listeners);
            this.appContext.startup = this;
            this.appContext.screen = screen;
            document.body.appendChild(this.canvas.container);
            //var asteroidFactory = new AsteroidFactory();
            //this.objects = this.objects.concat(asteroidFactory.createMulty(10, this.width, this.height));
            var timer = new Timer(function () {
                Startup.run()
            }, 20);
            timer.start();
        },
        run: function () {
            var startDate = new Date();
            console.log('start',startDate.getTime());
            var canvas = this.canvas;
            var context = canvas.getContext(0);
            context.clearRect(0, 0, canvas.width, canvas.height);
            var length = this.objects.length;
            var shift = this.spaceShip ? this.canvasWindow.getShift(this.spaceShip) : {x: 0, y: 0};
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
            var endDate = new Date();
            var time = 20 - (endDate.getMilliseconds() - startDate.getMilliseconds());
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