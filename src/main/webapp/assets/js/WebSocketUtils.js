Engine.define('WebSocketUtils', {
    getSocket: function (url, onOpen, onMessage, onClose, onError) {
        var host;
        var interval;
        if (window.location.protocol == 'http:') {
            host = 'ws://' + window.location.host + '/' + url;
        } else {
            host = 'wss://' + window.location.host + '/' + url;
        }
        var socket;
        if ('WebSocket' in window) {
            socket = new WebSocket(host);
        } else if ('MozWebSocket' in window) {
            socket = new MozWebSocket(host);
        } else {
            return null;
        }

        socket.onerror = function(e) {
            clearInterval(interval);
            onError(e);
        };

        socket.onopen = function (e) {
            onOpen(e);
            interval = setInterval(function () {
                // Prevent server read timeout.
                socket.send('ping');
            }, 5000);
        };

        socket.onclose = function(e){
            clearInterval(interval);
            onClose(e)
        };
        socket.onmessage = onMessage;
        return socket;
    }
});