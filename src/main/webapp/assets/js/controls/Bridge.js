Engine.define("Bridge", ['Asteroid', 'Vector', 'Bullet', 'Point', 'SpaceShip', 'Gun', 'WebSocketUtils'], function () {
    var WebSocketUtils = Engine.require('WebSocketUtils');
    var SpaceShip = Engine.require('SpaceShip');
    var Asteroid = Engine.require('Asteroid');
    var Vector = Engine.require('Vector');
    var Bullet = Engine.require('Bullet');
    var Point = Engine.require('Point');
    var Gun = Engine.require('Gun');

    function findSpaceShip(objects, id) {
        for (var i = 0; i < objects.length; i++) {
            if (objects[i].id == id && objects[i] instanceof SpaceShip) {
                return objects[i];
            }
        }
        return null;
    }

    function Bridge(context) {
        this.context = context;
    }

    Bridge.prototype.onOpen = function (r) {
        console.log('on open', r)
    };
    Bridge.prototype.onClose = function (r) {
        console.log('on close', r)
    };

    Bridge.prototype.onError = function (r) {
        console.log('on error', r)
    };
    Bridge.prototype.onMessage = function (r) {
        var data = r.data;
        try {
            var object = JSON.parse(data);
            switch (object.type) {
                case "fullUpdate":
                    var startDate = new Date();
                    console.log("full update starts: ", startDate.getMilliseconds());
                    var oldObjects = this.context.objects;
                    var objects = [];
                    this.context.objects = objects;
                    this.context.startup.objects = objects;
                    var incoming = object.objects;
                    var length = incoming.length;
                    this.context.startup.width = object.x;
                    this.context.startup.height = object.y;
                    var me = this;
                    while (length--) {
                        var source = incoming[length];
                        var mapped;
                        switch (source.type) {
                            case 'asteroid':
                                mapped = new Asteroid(source.x, source.y, source.points.map(function (v) {
                                    return new Point(v.x, v.y)
                                }));
                                mapped.rotationSpeed = source.rotationSpeed;
                                mapped.angle = source.angle;
                                mapped.vector = new Vector(source.vector.x, source.vector.y);
                                mapped.id = source.id;
                                break;
                            case 'bullet':
                                mapped = new Bullet(source.x, source.y, source.angle);
                                mapped.vector = new Vector(source.vector.x, source.vector.y);
                                mapped.id = source.id;
                                if (source.spaceShip) {
                                    var spaceShip = findSpaceShip(source.ship);
                                    if (spaceShip) {
                                        mapped.ship = spaceShip;
                                    }
                                }
                                break;
                            case 'ship':
                                mapped = new SpaceShip(source.x, source.y, this.context);
                                mapped.angle = source.angle;
                                mapped.vector = new Vector(source.vector.x, source.vector.y);
                                mapped.id = source.id;
                                if (object.id === mapped.id) {
                                    var old = this.context.startup.spaceShip;
                                    if (old) {
                                        old.unListen();
                                    }
                                    this.context.startup.spaceShip = mapped;
                                    mapped.listen(function (item) {
                                        me.context.startup.objects.push(item);
                                    })
                                }
                                var oldSpaceShip = findSpaceShip(oldObjects, mapped.id);
                                mapped.turnToLeft = oldSpaceShip ? (oldSpaceShip.turnToLeft ? true : source.turn == -1) : source.turn == -1;
                                mapped.turnToRight = oldSpaceShip ? (oldSpaceShip.turnToRight ? true : source.turn == 1) : source.turn == 1;
                                ;
                                mapped.guns = source.guns.map(function (v) {
                                    var gun = new Gun(v.x, v.y);
                                    gun.angle = v.angle || 0;
                                    gun.color = v.color || 'red';
                                    gun.reload = v.reload || false;
                                    return gun;
                                });
                                break;
                            default:
                                throw "unknown class";
                        }
                        objects.push(mapped);
                    }
                    var endDate = new Date();
                    console.log("full update ends: ", endDate.getMilliseconds(), endDate.getMilliseconds() - startDate.getMilliseconds());
                    break;
            }
        } catch (e) {
            console.log(e);
        }
    };

    return Bridge;
});