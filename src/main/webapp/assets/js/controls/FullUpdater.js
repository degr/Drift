Engine.define("FullUpdater", ['Asteroid', 'Vector', 'Bullet', 'Explosion', 'Point', 'ObjectsSearch', 'SpaceShip', 'Gun', 'RefinaryBase'], function () {

    var ObjectsSearch = Engine.require('ObjectsSearch');
    var SpaceShip = Engine.require('SpaceShip');
    var Explosion = Engine.require('Explosion');
    var Asteroid = Engine.require('Asteroid');
    var RefinaryBase = Engine.require('RefinaryBase');
    var Vector = Engine.require('Vector');
    var Bullet = Engine.require('Bullet');
    var Point = Engine.require('Point');
    var Gun = Engine.require('Gun');

    function FullUpdater(context, spaceSheepUpdater) {
        this.context = context;
        this.spaceSheepUpdater = spaceSheepUpdater;
    }

    FullUpdater.prototype.prepareObjects = function (incoming) {
        var length = incoming.length;
        var objects = [];
        while (length--) {
            objects.push(
                this.map(
                    incoming[length],
                    null/*no need to pass something, because players ship in game*/,
                    objects
                )
            );
        }
        return objects;
    };

    FullUpdater.prototype.update = function (object, id) {
        var startDate = new Date();
        var objects = [];
        var incoming = object.objects;
        var length = incoming.length;
        //var count = this.context.space.objects.filter(function(v){return v instanceof Asteroid}).length;
        //var nCount = 0;
        while (length--) {
            var source = incoming[length];
            //if(source.type === 'asteroid') {
           //     nCount++;
           // }

            objects.push(
                this.map(source, id)
            );
        }
        //console.log('new: ' + nCount +"; old: " + count);
        var endDate = new Date();
        this.context.space.height = object.y;
        this.context.space.width = object.x;
        this.context.space.objects = objects;
        console.log("full update take: ", endDate.getMilliseconds() - startDate.getMilliseconds());
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
            case 'refinary':
                mapped = new RefinaryBase(source.x, source.y, source.angle);
                break;
            default:
                throw "unknown class";
        }
        return mapped;
    };

    return FullUpdater;
});