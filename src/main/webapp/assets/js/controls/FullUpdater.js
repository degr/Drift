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

    FullUpdater.prototype.update = function (object) {
        var startDate = new Date();
        console.log("full update starts: ", startDate.getMilliseconds());
        var objects = [];
        var oldObjects = this.context.space.objects;
        this.context.objects = objects;
        this.context.space.objects = objects;
        var incoming = object.objects;
        var length = incoming.length;
        this.context.space.width = object.x;
        this.context.space.height = object.y;
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
                        var spaceShip = ObjectsSearch.findSpaceShip(oldObjects, source.ship);
                        if (spaceShip) {
                            mapped.ship = spaceShip;
                        }
                    }
                    break;
                case 'ship':
                    mapped = this.spaceSheepUpdater.update(source, object.id);
                    break;
                case 'explosion':
                    mapped = new Explosion(source.x, source.y, new Vector(source.vector.x, source.vector.y), source.radius);

                    break;
                default:
                    throw "unknown class";
            }
            objects.push(mapped);
        }
        var endDate = new Date();
        console.log("full update ends: ", endDate.getMilliseconds(), endDate.getMilliseconds() - startDate.getMilliseconds());
    };



    return FullUpdater;
});