Engine.define('FreeSpace', ['Bridge', 'Space', 'WebSocketUtils', 'Profile'], function(){

    var WebSocketUtils = Engine.require('WebSocketUtils');
    var Profile = Engine.require('Profile');
    var Bridge = Engine.require('Bridge');
    var Space = Engine.require('Space');

    var FreeSpace = {
        clb: null,
        space: null,
        started: false,
        start: function(clb) {

            this.clb = clb;
            this.started = true;
            this.space = new Space(5000, 5000);
            var context = {
                space: this.space,
                freeSpace: true
            };
            var bridge = new Bridge(context);
            context.socket = WebSocketUtils.getSocket(
                Profile.WS_URL,
                function (r) {
                    bridge.onOpen(r)
                },
                function (r) {
                    bridge.onMessage(r)
                },
                function (r) {
                    bridge.onClose(r)
                },
                function (r) {
                    bridge.onError(r)
                }
            );

            context.bridge = bridge;
            this.space.start();
        },
        stop: function() {
            this.space.stop();
            this.started = false;
            if(this.clb) {
                this.clb();
            }
        }
    };



    return FreeSpace;
});