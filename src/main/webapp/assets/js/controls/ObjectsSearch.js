Engine.define('ObjectsSearch', ['SpaceShip'], function(){

    var SpaceShip = Engine.require('SpaceShip');

    return {
        findSpaceShip: function findSpaceShip(objects, id) {
            for (var i = 0; i < objects.length; i++) {
                if (objects[i].id === id && objects[i] instanceof SpaceShip) {
                    return objects[i];
                }
            }
            return null;
        }
    }
});