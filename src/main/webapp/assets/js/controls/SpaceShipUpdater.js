Engine.define("SpaceShipUpdater", ['SpaceShip', 'ObjectsSearch', 'Gun', 'Vector'], function(){

    var ObjectsSearch = Engine.require('ObjectsSearch');
    var SpaceShip = Engine.require('SpaceShip');
    var Vector = Engine.require('Vector');
    var Gun = Engine.require('Gun');

    function SpaceShipUpdater(context) {
        this.context = context;
    }

    SpaceShipUpdater.prototype.simpleUpdate = function(string) {
        var data = string.split('|');
        var id = parseInt(data[0]);
        var spaceShip = ObjectsSearch.findSpaceShip(this.context.space.objects, id);
        if(spaceShip !== null) {
            for(var i = 1; i < data.length; i++) {
                var pair = data[i].split(':');
                var value = pair[1];
                switch (pair[0]) {
                    case 'a':
                        spaceShip.hasAcceleration = value === '1';
                        break;
                    case 't':
                        if(value === '1') {
                            spaceShip.turnToLeft = false;
                            spaceShip.turnToRight = true;
                        } else if(value === '-1') {
                            spaceShip.turnToLeft = true;
                            spaceShip.turnToRight = false;
                        } else {
                            spaceShip.turnToLeft = false;
                            spaceShip.turnToRight = false;
                        }
                        break;
                        break;
                    case 'f':
                        spaceShip.fireStarted = value === '1';
                        break;
                }
            }
        }

    };
    SpaceShipUpdater.prototype.update = function(source, id) {
        var context = this.context;
        var out = new SpaceShip(source.x, source.y, context);
        out.angle = source.angle;
        out.vector = new Vector(source.vector.x, source.vector.y);
        out.id = source.id;
        out.invincible = source.invincible;
        var old = null;
        if (id === out.id) {
            old = context.space.spaceShip;
            if (old) {
                old.unListen();
            }
            context.space.spaceShip = out;
            var keyDownListener = function(event) {
                var keyCode = event.keyCode;
                if(keyCode === 39){
                    event.preventDefault();
                    if(!out.turnToLeft) {
                        context.socket.send("turn:1")
                    } else {
                        context.socket.send("turn:0")
                    }
                } else if(keyCode === 37){
                    event.preventDefault();
                    if(!out.turnToRight) {
                        context.socket.send("turn:-1")
                    } else {
                        context.socket.send("turn:0")
                    }
                } else if(keyCode === 38){
                    event.preventDefault();
                    context.socket.send("accelerate:1");
                } else if(keyCode === 40){
                    event.preventDefault();
                } else if(keyCode === 32) {
                    context.socket.send("fire:1")
                }
            };
            var keyUpListener = function(event) {
                var keyCode = event.keyCode;
                if(keyCode === 39){
                    event.preventDefault();
                    if(out.turnToLeft) {
                        context.socket.send("turn:-1")
                    } else {
                        context.socket.send("turn:0")
                    }
                } else if(keyCode === 37){
                    event.preventDefault();
                    if(out.turnToRight) {
                        context.socket.send("turn:1")
                    } else {
                        context.socket.send("turn:0")
                    }
                } else if(keyCode === 38){
                    event.preventDefault();
                    context.socket.send("accelerate:0");
                } else if(keyCode === 32) {
                    context.socket.send("fire:0");
                }
            };
            out.listen(keyDownListener, keyUpListener);
        }

        out.turnToLeft = source.turn === -1;
        out.turnToRight = source.turn === 1;

        out.guns = source.guns.map(function (v) {
            var gun = new Gun(v.x, v.y);
            gun.angle = v.angle || 0;
            gun.color = v.color || 'red';
            gun.reload = v.reload || false;
            return gun;
        });
        return out;
    };

    return SpaceShipUpdater;
});