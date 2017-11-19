Engine.define("Bridge", ['WebSocketUtils', 'SpaceShipUpdater', 'FullUpdater', 'Asteroid'], function () {

    var SpaceShipUpdater = Engine.require('SpaceShipUpdater');
    var WebSocketUtils = Engine.require('WebSocketUtils');
    var FullUpdater = Engine.require('FullUpdater');
    var Asteroid = Engine.require('Asteroid');//todo delete


    function Bridge(context) {
        this.context = context;
        var spaceShipUpdater = new SpaceShipUpdater(context);
        this.fullUpdater = new FullUpdater(context, spaceShipUpdater);
        this.spaceShipUpdater = spaceShipUpdater;
    }

    Bridge.prototype.onOpen = function (r) {
        this.context.socket.send('info')
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
            var fullHouse = false;
            if(isFinite(object)) {
                console.log('js: ' + this.context.space.objects.filter(function(o){return o instanceof Asteroid}).length + "; java: " + object)
            } else {
                switch (object.type) {
                    case "fullUpdate":
                        fullHouse = true;
                        this.fullUpdater.update(object, this.context.spaceShipId);
                        this.context.space.draw(this.context);
                        break;
                    case 'ships':
                        var ships = object.ships;
                        if (ships) {
                            for (var i = 0; i < ships.length; i++) {
                                this.spaceShipUpdater.simpleUpdate(ships[i]);
                            }
                        }
                        var newAsteriods = 0;
                        if (object.newObjects) {
                            newAsteriods = object.newObjects.filter(function(o){return o && o.type === 'asteroid'}).length;
                            if(newAsteriods > 0) {
                                console.log('incoming asteroids: ' + newAsteriods);
                            }
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
                        }
                        if(newAsteriods > 0) {
                            console.log(((new Date()).getTime()) + ' js: ' + this.context.space.objects.filter(function (o) {
                                    return o instanceof Asteroid && o.isAlive()
                                }).length + "; java: " + object.acount)
                        }*/
                        break;
                    case 'info':
                        this.context.spaceShipId = object.spaceShipId;
                        this.context.playerId = object.playerId;
                        break;
                    default:
                        throw "Unknown update object. Type: " + object.type;
                }
            }
        } catch (e) {
            console.log(e);
        }
        if(!fullHouse) {
            this.context.space.run();
        }
    };


    return Bridge;
});