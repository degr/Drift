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
        modal: null,
        start: function () {
            var me = this;

            this.modal = new Popup({
                controlMinimize: false,
                controlClose: false,
                title: Dom.el('h1', null, 'Please select game type')}
            );
            var clb = function(){
                if(!me.modal.isOpen) {
                    me.modal.show();
                }
            };
            var form = Dom.el('div', null, [
                Dom.el('div', null, [
                    Dom.el('input', {type: 'button', value: 'Sandbox', onclick: function(){
                        Engine.load('Sandbox', function(){
                            var Sandbox = Engine.require('Sandbox');
                            if(Sandbox.started) {
                                Sandbox.stop();
                            }
                            me.modal.hide();
                            Sandbox.start(clb);
                        });
                    }, class: 'button-sandbox'}),
                    Dom.el('input', {type: 'button', value: 'Free Space', onclick: function(){
                        Engine.load('FreeSpace', function(){
                            me.modal.hide();
                            var FreeSpace = Engine.require('Sandbox');
                            FreeSpace.start(clb);
                        })
                    }, class: 'button-space'})
                ])
            ]);
            this.modal.setContent(form);
            this.modal.show();
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


        },

    };


    return Startup;
});