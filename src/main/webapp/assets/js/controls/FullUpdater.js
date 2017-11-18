Engine.define("FullUpdater", ['Asteroid', 'Vector', 'Bullet', 'Explosion', 'Point', 'ObjectsSearch', 'SpaceShip', 'Gun'], function () {

    var ObjectsSearch = Engine.require('ObjectsSearch');
    var SpaceShip = Engine.require('SpaceShip');
    var Explosion = Engine.require('Explosion');
    var Asteroid = Engine.require('Asteroid');
    var Vector = Engine.require('Vector');
    var Bullet = Engine.require('Bullet');
    var Point = Engine.require('Point');
    var Gun = Engine.require('Gun');

    function FullUpdater(context, spaceSheepUpdater) {
        this.context = context;
        this.spaceSheepUpdater = spaceSheepUpdater;
    }

    FullUpdater.prototype.append = function (incoming) {
        var length = incoming.length;
        var objects = this.context.space.objects;
        while (length--) {
            objects.push(
                this.map(
                    incoming[length],
                    null/*no need to pass something, because players ship in game*/,
                    objects
                )
            );
        }
    };

    FullUpdater.prototype.update = function (object, id) {
        var startDate = new Date();
        console.log("full update starts: ", startDate.getMilliseconds());
        var objects = [];
        var oldObjects = this.context.space.objects;
        this.context.space.objects = objects;
        var incoming = object.objects;
        var length = incoming.length;
        this.context.space.width = object.x;
        this.context.space.height = object.y;
        while (length--) {
            var source = incoming[length];
            objects.push(
                this.map(source, id)
            );
        }
        var endDate = new Date();
        console.log("full update ends: ", endDate.getMilliseconds(), endDate.getMilliseconds() - startDate.getMilliseconds());
    };

    FullUpdater.prototype.map = function(source, playerId) {
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
                mapped.ship = source.ship;
                mapped.points = [
                    new Point(source.points[0].x, source.points[0].y),
                    new Point(source.points[1].x, source.points[1].y)
                ];
                mapped.oldPoint = mapped.points[0];
                break;
            case 'ship':
                mapped = this.spaceSheepUpdater.update(source, playerId);
                break;
            case 'explosion':
                mapped = new Explosion(source.x, source.y, new Vector(source.vector.x, source.vector.y), source.radius);

                break;
            default:
                throw "unknown class";
        }
        return mapped;
    }

    return FullUpdater;
});