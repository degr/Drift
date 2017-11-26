Engine.define("PathBuilder", function(){
    function PathBuilder(version) {
        this.version = version;
    }

    PathBuilder.prototype.buildPath = function(module) {
        var path;
        switch (module) {
            case "Startup":
            case "Profile":
            case "WebSocketUtils":
                path = module;
                break;
            case 'Bridge':
            case 'FullUpdater':
            case 'ObjectsSearch':
            case 'SpaceShipUpdater':
                path = "controls/" + module;
                break;
            case 'Line':
            case 'Point':
            case 'Vector':
            case 'Geometry':
                path = "geometry/" + module;
                break;
            case 'Timer':
            case 'Space':
            case 'Sandbox':
            case 'FreeSpace':
            case 'MassUtils':
            case 'CanvasWindow':
                path = "utils/" + module;
                break;
            case "BaseObject":
            case "RelativePointsObject":
                path = "space/" + module;
                break;
            case "RefinaryBase":
            case "LandingLight":
                path = "space/base/" + module;
                break;
            case "Gun":
            case "Bullet":
            case "SpaceShip":
                path = "space/spaceship/" + module;
                break;
            case 'Explosion':
            case 'ExplosionRay':
                path = "space/explosion/" + module;
                break;
            case 'Asteroid':
            case 'AsteroidFactory':
                path = "space/asteroids/" + module;
                break;
        }
        return (path ? 'assets/js/' + path + ".js?version=" + this.version : '');
    };

    return PathBuilder;
});