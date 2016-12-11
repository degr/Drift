Engine.define('CanvasWindow', ['ScreenUtils', 'Point'], function () {

    var Point = Engine.require('Point');
    var ScreenUtils = Engine.require('ScreenUtils');

    function CanvasWindow(width, height) {
        this.width = width;
        this.height = height;
        this.window = ScreenUtils.window();
    }


    CanvasWindow.prototype.resize = function (win, width, height) {
        this.window = win;
        this.width = width || this.width;
        this.height = height || this.height;
    };
    CanvasWindow.prototype.getShift = function (object) {
        var centerX = this.window.width / 2;
        var centerY = this.window.height / 2;
        return new Point(
            CanvasWindow.calculateOffset(centerX, this.width, object.x, this.window.width),
            CanvasWindow.calculateOffset(centerY, this.height, object.y, this.window.height)
        )
    };

    CanvasWindow.calculateOffset = function(center, size, object, win) {
        if(object < center) {
            return 0;
        } else if(object > size - center) {
            return win - size;
        } else {
            return center - object;
        }
    };

    return CanvasWindow;
});