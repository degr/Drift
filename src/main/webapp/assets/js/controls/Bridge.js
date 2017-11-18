Engine.define("Bridge", ['WebSocketUtils', 'SpaceShipUpdater', 'FullUpdater'], function () {

    var SpaceShipUpdater = Engine.require('SpaceShipUpdater');
    var WebSocketUtils = Engine.require('WebSocketUtils');
    var FullUpdater = Engine.require('FullUpdater');


    function Bridge(context) {
        this.context = context;
        var spaceShipUpdater = new SpaceShipUpdater(context);
        this.fullUpdater = new FullUpdater(context, spaceShipUpdater);
        this.spaceShipUpdater = spaceShipUpdater;
    }

    Bridge.prototype.onOpen = function (r) {
        console.log('on open', r)
    };
    Bridge.prototype.onClose = function (r) {
        console.log('on close', r)
    };

    Bridge.prototype.onError = function (r) {
        alert('error!');
        console.log('on error', r)
    };
    Bridge.prototype.onMessage = function (r) {
        var data = r.data;
        try {


            var object = JSON.parse(data);
            if(object !== 1) {
                switch (object.type) {
                    case "fullUpdate":
                        this.fullUpdater.update(object);
                        break;
                    case 'ships':
                        var ships = object.ships;
                        if (ships) {
                            for (var i = 0; i < ships.length; i++) {
                                this.spaceShipUpdater.simpleUpdate(ships[i]);
                            }
                        }
                        if (object.newObjects) {
                            this.fullUpdater.append(object.newObjects);
                        }
                        /*var obj = this.context.space.objects;
                        var l = obj.length;
                        while(l--) {
                            if(obj[l].isGhost) {
                                obj.splice(l, 1);
                            }
                        }
                        if(object.ghosts) {
                            for(var i = 0; i < object.ghosts.length; i++) {
                                obj.push(this.spaceShipUpdater.createGhost(object.ghosts[i]));
                            }
                        }*/
                        break;
                    default:
                        throw "Unknown update object. Type: " + object.type;
                }
            }
        } catch (e) {
            console.log(e);
        }

        this.context.space.run();
    };


    return Bridge;
});