Engine.define("SpaceShipUpdater", ['SpaceShip', 'ObjectsSearch', 'Gun', 'Vector'], function(){

    var ObjectsSearch = Engine.require('ObjectsSearch');
    var SpaceShip = Engine.require('SpaceShip');
    var Vector = Engine.require('Vector');
    var Gun = Engine.require('Gun');

    function SpaceShipUpdater(context) {
        this.context = context;
    }

    SpaceShipUpdater.prototype.updatePosition = function (spaceShip, vSX, vSY) {
        var vX = parseFloat(vSX);
        var vY = parseFloat(vSY);
        var dX = vX - spaceShip.vector.x;
        var dY = vY - spaceShip.vector.y;
        spaceShip.vector.x = vX;
        spaceShip.vector.y = vY;
        spaceShip.x += dX;
        spaceShip.y += dY;
    };
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
                        this.updatePosition(spaceShip, pair[2], pair[3]);
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
                        spaceShip.angle = parseFloat(pair[2]);
                        break;
                        break;
                    case 'f':
                        spaceShip.fireStarted = value === '1';
                        this.updatePosition(spaceShip, pair[2], pair[3]);
                        break;
                    case 'i':
                        spaceShip.invincible = value === '1';
                        break;
                }
            }
        }

    };
    SpaceShipUpdater.prototype.update = function(source, id) {
        var context = this.context;
        var spaceShip = new SpaceShip(source.x, source.y, context);
        spaceShip.angle = source.angle;
        spaceShip.vector = new Vector(source.vector.x, source.vector.y);
        spaceShip.id = source.id;
        spaceShip.invincible = source.invincible;
        var old = null;
        if (id === spaceShip.id) {
            old = context.space.spaceShip;
            if (old) {
                old.unListen();
            }
            context.space.spaceShip = spaceShip;
            var keyDownListener = function(event) {
                var keyCode = event.keyCode;
                if(keyCode === 39){
                    //right button
                    event.preventDefault();
                    if(spaceShip.turnToLeft) {
                        if(!spaceShip.turnToRight) {
                            context.socket.send("turn:0");
                        }
                    } else {
                        if(!spaceShip.turnToRight) {
                            context.socket.send("turn:1");
                        }
                    }
                } else if(keyCode === 37){
                    //left button
                    event.preventDefault();
                    if(spaceShip.turnToRight) {
                        if(!spaceShip.turnToLeft) {
                            context.socket.send("turn:0");
                        }
                    } else {
                        if(!spaceShip.turnToLeft) {
                            context.socket.send("turn:-1");
                        }
                    }
                } else if(keyCode === 38){
                    event.preventDefault();
                    if(!spaceShip.hasAcceleration) {
                        context.socket.send("accelerate:1");
                    }
                } else if(keyCode === 40){
                    event.preventDefault();
                } else if(keyCode === 32) {
                    if(!spaceShip.fireStarted) {
                        spaceShip.fireStarted = true;
                        context.socket.send("fire:1")
                    }
                }
            };
            var keyUpListener = function(event) {
                var keyCode = event.keyCode;
                if(keyCode === 39) {
                    //right button
                    event.preventDefault();
                    if(spaceShip.turnToLeft) {
                        context.socket.send("turn:-1")
                    } else {
                        context.socket.send("turn:0")
                    }
                } else if(keyCode === 37){
                    event.preventDefault();
                    if(spaceShip.turnToRight) {
                        context.socket.send("turn:1")
                    } else {
                        context.socket.send("turn:0")
                    }
                } else if(keyCode === 38){
                    event.preventDefault();
                    context.socket.send("accelerate:0");
                } else if(keyCode === 32) {
                    spaceShip.fireStarted = false;
                    context.socket.send("fire:0");
                }
            };
            spaceShip.listen(keyDownListener, keyUpListener);
        }

        spaceShip.turnToLeft = source.turn === -1;
        spaceShip.turnToRight = source.turn === 1;

        spaceShip.guns = source.guns.map(function (v) {
            var gun = new Gun(v.x, v.y);
            gun.angle = v.angle || 0;
            gun.color = 'red';
            gun.reload = v.reload || false;
            return gun;
        });
        return spaceShip;
    };

    /*SpaceShipUpdater.prototype.createGhost = function(data) {
        var s = new SpaceShip(data.x, data.y, this.context);
        s.isGhost = true;
        s.alive = data.alive;
        s.angle = data.angle;
        s.fireStarted = data.fireStarted;
        s.hasAcceleration = data.hasAcceleration;
        s.invincible = true;
        s.turn = data.turn;
        s.vector = new Vector(data.vector.x, data.vector.y);

        return s;
    };*/

    return SpaceShipUpdater;
});