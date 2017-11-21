Engine.define('Space', ['ScreenUtils', 'Dom', 'StringUtils', 'BaseObject', 'Timer', 'CanvasWindow', 'LayeredCanvas', 'Geometry'], function () {

    var LayeredCanvas = Engine.require('LayeredCanvas');
    var CanvasWindow = Engine.require('CanvasWindow');
    var ScreenUtils = Engine.require('ScreenUtils');
    var StringUtils = Engine.require('StringUtils');
    var BaseObject = Engine.require('BaseObject');
    var Geometry = Engine.require('Geometry');
    var Timer = Engine.require('Timer');
    var Dom = Engine.require('Dom');

    /**
     * @param width
     * @param height
     * @constructor
     */
    function Space(width, height) {
        var me = this;
        this.width = width;
        this.height = height;
        this.canvas = null;
        this.spaceShip = null;
        this.canvasWindow = null;
        this.objects = [];
        this.appContext = {};
        this.listeners = {
            onresize: function () {
                var screen = ScreenUtils.window();
                me.appContext.screen = screen;
                me.canvas.resize(screen.width, screen.height);
                me.canvasWindow.resize(screen);
            }
        }
    }

    /**
     * init method prepare Space object for correct work
     */
    Space.prototype.init = function () {
        var screen = ScreenUtils.window();
        var me = this;
        this.canvas = new LayeredCanvas(2, screen.width, screen.height);
        this.canvasWindow = new CanvasWindow(me.width, me.height);
        Dom.addListeners(me.listeners);
        this.appContext.space = this;
        this.appContext.screen = screen;
        document.body.appendChild(this.canvas.container);
    };
    /**
     * start method run Space as background process
     */
    Space.prototype.start = function () {
        this.init();
        var me = this;

        this.timer = new Timer(function () {
            me.run()
        }, 20, StringUtils.unique());
        this.timer.start();
    };

    Space.prototype.run = function () {
        var me = this;
        var canvas = this.canvas;
        var context = canvas.getContext(0);
        context.clearRect(0, 0, canvas.width, canvas.height);
        var length = this.objects.length;
        var shift = this.spaceShip ? this.canvasWindow.getShift(this.spaceShip) : {x: 0, y: 0};
        this.appContext.shift = shift;
        context.beginPath();
        context.strokeStyle = "red";
        context.lineWidth = 10;
        context.rect(shift.x, shift.y, me.width, me.height);
        context.stroke();
        context.lineWidth = 1;

        while (length--) {
            var obj = this.objects[length];
            if (obj.isAlive()) {
                //if(!obj.isGhost) {
                    obj.update();
                //}
                if (obj.x > me.width) {
                    obj.x = obj.x - me.width;
                } else if (obj.x < 0) {
                    obj.x = me.width + obj.x;
                }
                if (obj.y > me.height) {
                    obj.y = obj.y - me.height;
                } else if (obj.y < 0) {
                    obj.y = me.height + obj.y;
                }
                obj.draw(context, this.appContext);
                me.calculateImpacts(obj);

            } else {
                this.objects.splice(length, 1);
            }
        }
    };

    Space.prototype.draw = function() {
        var length = this.objects.length;
        var shift = this.spaceShip ? this.canvasWindow.getShift(this.spaceShip) : {x: 0, y: 0};
        this.appContext.shift = shift;
        while (length--) {
            var obj = this.objects[length];
            obj.draw(this.canvas.getContext(0), this.appContext);
        }
    };

    Space.prototype.stop = function () {
        Dom.removeListeners(this.listeners);
        this.timer.stop();
        this.spaceShip.unListen();
        this.canvas.container.remove();
    };

    Space.prototype.calculateImpacts = function (obj) {
        var length = this.objects.length;
        while (length--) {
            if (this.objects[length] !== obj) {
                var distance = Geometry.getDistance(obj, this.objects[length]);
                if (distance < 300) {
                    var impact = obj.hasImpact(this.objects[length]);
                    if (impact) {
                        var imp1 = obj.onImpact(this.objects[length]);
                        var imp2 = this.objects[length].onImpact(obj);
                        this.objects = this.objects.concat(imp1, imp2);
                    }
                }
            }
        }
    };


    return Space;
});