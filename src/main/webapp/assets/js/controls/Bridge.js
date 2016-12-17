Engine.define("Bridge", 'WebSocketUtils', function(){
    var WebSocketUtils = Engine.require('WebSocketUtils');
    function Bridge() {

    }

    Bridge.prototype.onOpen = function(r){
        console.log('on open', r)
    };
    Bridge.prototype.onClose = function(r) {
        console.log('on close', r)
    };

    Bridge.prototype.onError = function(r) {
        console.log('on error', r)
    };
    Bridge.prototype.onMessage = function(r) {
        console.log('on message', r)
    };

    return Bridge;
});